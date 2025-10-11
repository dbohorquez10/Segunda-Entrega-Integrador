# Proyecto Integrador – Sistema ICA-FITO

## Descripción general

**ICA-FITO** es una aplicación de escritorio desarrollada en **Java Swing**, conectada a una base de datos **Oracle**, diseñada para facilitar los procesos de inspección y control fitosanitario del **Instituto Colombiano Agropecuario (ICA)**.  

El sistema permite registrar y administrar **cultivos, plagas y lotes**, así como **asignar técnicos** para generar **reportes de plagas** relacionados con cultivos específicos.  
Aplica conceptos de **Programación Orientada a Objetos (POO)** y utiliza la arquitectura **MVC (Modelo–Vista–Controlador)** para garantizar modularidad, claridad y mantenimiento del código.

El entorno visual está construido con **Swing**, manteniendo una estética uniforme en **tonos verdes ICA**, navegación unificada mediante **CardLayout** y botón global de **Cerrar sesión**.

---

##  (Aquí va la estructura del código fuente)
```plaintext
src/
│
├── modelo/
│   ├── ConexionBD.java            # Conexión Oracle (remota)
│   ├── Usuario.java / UsuarioDAO.java
│   ├── Cultivo.java / CultivoDAO.java
│   ├── Plaga.java / PlagaDAO.java
│   ├── Lote.java / LoteDAO.java
│   ├── AsignacionLote.java / AsignacionLoteDAO.java
│   ├── ReportePlaga.java / ReportePlagaDAO.java
│   ├── Sesion.java / Rol.java
│
├── controlador/
│   ├── AuthControl.java
│   ├── LoteControl.java
│   ├── AsignacionLoteControl.java
│   ├── ReportePlagaControl.java
│
└── vista/
    ├── LoginFrame.java
    ├── MainAppFrame.java
    ├── UIStyle.java
    ├── CultivoPanel.java
    ├── PlagaPanel.java
    ├── LotePanel.java
    ├── AsignacionLotePanel.java
    ├── ReportePlagaPanel.java
    └── Refreshable.java
```

---

## Objetivos del sistema

- Implementar un sistema completo para el **registro y gestión de datos fitosanitarios**.  
- Controlar los **niveles de acceso** mediante roles (ADMIN, TÉCNICO, PRODUCTOR).  
- Permitir la **asignación de lotes** a técnicos para generar reportes validados.  
- Unificar todas las vistas en una sola ventana principal (sin ventanas emergentes).  
- Mantener una interfaz visual **clara, moderna y accesible**.

---

## Base de datos Oracle

**Parámetros de conexión actuales:**

- **Servidor:** `192.168.254.215`
- **Puerto:** `1521`
- **SID:** `orcl`
- **Usuario:** `ADMINICA`
- **Contraseña:** `adminica123`

**Gestor:** Oracle 10g XE  
**Conexión:** establecida por medio del archivo `ConexionBD.java` utilizando el driver `ojdbc11.jar`.

### Tablas principales
| Tabla | Descripción |
|--------|--------------|
| `USUARIO` | Usuarios del sistema (nombre, contraseña, rol, estado). |
| `ROL` | Define los roles de usuario (ADMIN, TECNICO, PRODUCTOR). |
| `CULTIVO` | Catálogo de cultivos. |
| `PLAGA` | Catálogo de plagas. |
| `CULTIVO_PLAGA` | Relación entre cultivos y las plagas que los afectan. |
| `LOTE` | Información detallada de cada lote agrícola. |
| `ASIGNACION_LOTE` | Asociación entre técnicos y lotes asignados. |
| `REPORTE_PLAGA` | Reportes generados por técnicos ICA. |

### Relaciones destacadas
- `USUARIO.ROL_ID → ROL.ID`
- `ASIGNACION_LOTE.ID_TECNICO → USUARIO.ID`
- `ASIGNACION_LOTE.ID_LOTE → LOTE.ID_LOTE`
- `REPORTE_PLAGA.ID_LOTE → LOTE.ID_LOTE`
- `REPORTE_PLAGA.ID_PLAGA → PLAGA.ID_PLAGA`
- `CULTIVO_PLAGA` vincula `CULTIVO` y `PLAGA`.

---

## Roles y permisos

| Rol | Descripción | Acciones principales |
|------|--------------|----------------------|
| **ADMIN** | Administrador general del sistema. | CRUD completo de cultivos, plagas, lotes y usuarios. Asigna lotes a técnicos. |
| **TECNICO** | Inspector fitosanitario del ICA. | Registra reportes de plagas sobre lotes asignados. Puede actualizar datos de sus lotes. |
| **PRODUCTOR** | Productor agrícola. | Consulta los datos de su lote y puede solicitar inspecciones (fase final). |

---

## Usuarios y roles preconfigurados (temporal)

> En esta entrega **no** existe el módulo para crear usuarios.  
> Para pruebas académicas, el sistema ya incluye **tres usuarios** con sus roles.

| Usuario | Contraseña | Rol       | Descripción / Permisos clave |
|--------:|:-----------|:----------|:------------------------------|
| `admin` | `1234`     | **ADMIN** | CRUD de Cultivos/Plagas/Lotes, **asigna** lotes a técnicos, puede crear reportes. |
| `tec1`  | `1234`     | **TECNICO** | Ve **solo sus lotes asignados**, puede **actualizar** datos del lote y **crear reportes**. |
| `prod1` | `1234`     | **PRODUCTOR** | Consulta de lotes y solicitud de inspecciones *(planeado para la entrega final)*. |

**Detalle técnico**
- La autenticación se realiza contra la tabla `USUARIO` (`USERNAME` / `PASSWORD_HASH`).
- Los roles se resuelven por `USUARIO.ROL_ID → ROL.ID` con valores `ADMIN`, `TECNICO`, `PRODUCTOR`.
- Para cambiar contraseñas o agregar usuarios **(temporalmente)** se debe hacer directamente en la base de datos por el administrador de BD.

---

## Descripción de componentes principales

### **Modelo**
Contiene las clases que representan las entidades del sistema (como `Cultivo`, `Plaga`, `Lote`, `Usuario`, etc.) y sus respectivas clases DAO (Data Access Object), que gestionan la conexión y consultas a Oracle mediante `ConexionBD.java`.  

Incluye además:
- `Sesion` → Controla el usuario actual y su rol.  
- `Rol` → Enum con los tres tipos de roles disponibles.

---

### **Controlador**
Gestiona la lógica de negocio y aplica reglas de permisos.  
Cada módulo tiene su controlador especializado:
- `AuthControl` → Manejo de autenticación y sesión.  
- `LoteControl` → Control de creación/edición según rol.  
- `AsignacionLoteControl` → Gestión de asignaciones técnico-lote.  
- `ReportePlagaControl` → Crea reportes validados según relación cultivo-plaga y asignación de técnico.

---

### **Vista**
Interfaz gráfica en Swing con diseño moderno, tonos verdes y navegación mediante `CardLayout`.  
- **LoginFrame**: inicio de sesión.  
- **MainAppFrame**: menú principal y contenedor de vistas.  
- **CultivoPanel**, **PlagaPanel**, **LotePanel**: gestión de datos.  
- **AsignacionLotePanel**: interfaz para relacionar técnicos con lotes.  
- **ReportePlagaPanel**: formulario para reportar plagas sin ingresar IDs manuales.  
- **UIStyle**: centraliza los estilos (botones, colores, tipografía).  
- **Refreshable**: interfaz auxiliar para actualizar datos al cambiar de panel.

---

## Flujo de ejecución del sistema

1. **Inicio de sesión:**  
   El usuario ingresa credenciales; `AuthControl` valida la información y crea una `Sesion`.  
   Según el rol, se habilitan los módulos correspondientes.

2. **Menú principal:**  
   Carga opciones dinámicamente.  
   - El administrador ve todos los módulos.  
   - El técnico solo ve “Lotes” y “Reportes”.  
   - El productor solo puede visualizar información (futuro).

3. **Gestión de datos:**  
   Módulos CRUD con validación y botones de acción (Refrescar, Nuevo, Eliminar, Guardar).

4. **Asignación de lotes:**  
   El administrador elige técnico y lote desde listas desplegables y los asocia.

5. **Reportes de plaga:**  
   El técnico elige lote → el sistema carga cultivo → el combo de plagas se filtra automáticamente.  
   Solo se editan **observaciones** y **porcentaje de infestación**.  
   Se valida que el lote esté asignado y que la plaga esté asociada al cultivo.

6. **Cierre de sesión:**  
   En cualquier vista, se puede cerrar sesión y regresar al login sin reiniciar la aplicación.

---

## Tecnologías utilizadas

| Categoría | Tecnología |
|------------|-------------|
| Lenguaje | Java SE 21 / JDK 24 |
| IDE | Apache NetBeans 19 |
| Base de datos | Oracle 10g XE |
| Driver JDBC | ojdbc11.jar |
| Interfaz | Java Swing |
| Arquitectura | MVC |
| Paradigma | Programación Orientada a Objetos (POO) |

---

## 📈 Estado del proyecto (50%)

| Módulo | Estado | Descripción |
|--------|:------:|-------------|
| Conexión Oracle | ✅ | Establecida con red universitaria |
| Login y roles | ✅ | Autenticación y permisos funcionales |
| CRUD Cultivo / Plaga | ✅ | Completos y probados |
| Lotes | ✅ | Creación (admin), actualización (técnico) |
| Asignación de lotes | ✅ | Filtrado dinámico y persistente |
| Reporte de plagas | ✅ | Validación por asignación y relación cultivo–plaga |
| Interfaz general | ✅ | Unificada en una sola ventana |
| Cierre de sesión | ✅ | Disponible desde cualquier panel |
| Productor / Inspecciones | ⏳ | Pendiente para versión final |

---

## Ejemplo de funcionamiento

```plaintext
--- LOGIN ---
Usuario: admin
Contraseña: 1234
[Ingresar]

--- MENÚ PRINCIPAL ---
Usuario: admin  |  Rol: ADMIN
[Cultivos] [Plagas] [Lotes] [Asignaciones] [Reportes] [Cerrar sesión]

--- MÓDULO DE ASIGNACIÓN ---
Técnico: tec1
Lote: L001
[Asignar] [Desasignar]
```

---

## Mejoras futuras

- Completar módulo de **Productor** (solicitudes de inspección).  
- Generar **reportes PDF** con los resultados de inspección.  
- Agregar **panel estadístico** de plagas y cultivos.  
- Incorporar **validaciones visuales** y alertas contextuales.  
- Mejorar **diseño responsivo** con íconos y logos institucionales.  

---

## Autores

**Darwing Yailang Bohórquez Jaimes**  
**Karen Rocío Cristancho Fajardo**  
Estudiantes de Ingeniería de Sistemas – Cuarto Semestre  
**Universidad de Investigación y Desarrollo (UDI)**  
📅 Octubre de 2025
