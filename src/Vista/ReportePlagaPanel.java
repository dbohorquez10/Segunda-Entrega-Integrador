/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Panel de Reporte de Plaga (Swing).
 * MVC: esta clase pertenece a la capa "Vista".
 *
 * Objetivo para esta entrega (50%):
 *  - Una sola pantalla (CardLayout en la ventana principal).
 *  - Lote se elige en combo:
 *      · ADMIN ve todos los lotes.
 *      · TÉCNICO solo ve sus lotes (ASIGNACION_LOTE → USUARIO).
 *  - El cultivo se autocompleta a partir del lote (solo lectura).
 *  - Plaga se elige en combo, filtrada por el cultivo del lote (CULTIVO_PLAGA).
 *  - El usuario SOLO escribe Observaciones y %Infestación (opcional).
 *  - Validaciones en el Controlador:
 *      · Rol (ADMIN/TÉCNICO).
 *      · Asignación (técnico ↔ lote).
 *      · Relación cultivo ↔ plaga.
 */

package vista;

import controlador.ReportePlagaControl;
import modelo.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ReportePlagaPanel extends JPanel {

    private final Sesion sesion;
    private final Runnable onBack;

    private final ReportePlagaControl control = new ReportePlagaControl();
    private final LoteDAO loteDAO = new LoteDAO();
    private final CultivoPlagaDAO cultivoPlagaDAO = new CultivoPlagaDAO();

    // Controles UI
    private JTextField txtIdReporte;   // si usas seq/trigger, puedes poner setEditable(false)
    private JComboBox<String> cbLote;
    private JTextField txtIdCultivo;   // solo lectura
    private JComboBox<String> cbPlaga;
    private JComboBox<String> cbSeveridad;
    private JTextField txtPorcentaje;
    private JTextArea txtObs;

    public ReportePlagaPanel(Sesion sesion, Runnable onBack) {
        this.sesion = sesion;
        this.onBack = onBack;
        buildUI();
        aplicarPermisos();
        cargarLotes(); // llena combo de lotes según rol
    }

    // ===================== UI =====================

    private void buildUI() {
        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        // Encabezado + volver
        JPanel header = UIStyle.sectionHeader(
                "Reporte de plaga",
                "Registre un evento de plaga sobre un lote asignado."
        );
        JButton btnBack = UIStyle.backButton();
        btnBack.addActionListener(e -> onBack.run());
        header.add(Box.createVerticalStrut(8));
        header.add(btnBack);
        add(header, BorderLayout.NORTH);

        // Formulario
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new TitledBorder("Datos del reporte"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        txtIdReporte = new JTextField(16);
        cbLote = new JComboBox<>();
        txtIdCultivo = new JTextField(16);
        txtIdCultivo.setEditable(false);
        cbPlaga = new JComboBox<>();
        cbSeveridad = new JComboBox<>(new String[]{"Leve", "Moderada", "Severa"});
        txtPorcentaje = new JTextField(8);
        txtObs = new JTextArea(5, 28);
        txtObs.setLineWrap(true);
        txtObs.setWrapStyleWord(true);
        JScrollPane spObs = new JScrollPane(txtObs);

        // Fila 1: ID Reporte
        c.gridx=0; c.gridy=y; form.add(new JLabel("ID Reporte"), c);
        c.gridx=1;            form.add(txtIdReporte, c); y++;

        // Fila 2: Lote (combo)
        c.gridx=0; c.gridy=y; form.add(new JLabel("Lote"), c);
        c.gridx=1;            form.add(cbLote, c); y++;

        // Fila 3: Cultivo (solo lectura)
        c.gridx=0; c.gridy=y; form.add(new JLabel("Cultivo (del lote)"), c);
        c.gridx=1;            form.add(txtIdCultivo, c); y++;

        // Fila 4: Plaga (combo filtrado por cultivo del lote)
        c.gridx=0; c.gridy=y; form.add(new JLabel("Plaga"), c);
        c.gridx=1;            form.add(cbPlaga, c); y++;

        // Fila 5: Severidad
        c.gridx=0; c.gridy=y; form.add(new JLabel("Severidad"), c);
        c.gridx=1;            form.add(cbSeveridad, c); y++;

        // Fila 6: % Infestación (opcional)
        c.gridx=0; c.gridy=y; form.add(new JLabel("% Infestación (0–100)"), c);
        c.gridx=1;            form.add(txtPorcentaje, c); y++;

        // Fila 7: Observaciones
        c.gridx=0; c.gridy=y; form.add(new JLabel("Observaciones"), c);
        c.gridx=1;            form.add(spObs, c); y++;

        // Acciones
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnGuardar = UIStyle.primaryButton("Guardar");
        acciones.add(btnLimpiar);
        acciones.add(btnGuardar);
        c.gridx=0; c.gridy=y; c.gridwidth=2; form.add(acciones, c);

        add(form, BorderLayout.CENTER);

        // Eventos
        cbLote.addActionListener(e -> onCambioLote());
        btnLimpiar.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardar());
    }

    private void aplicarPermisos() {
        boolean puede = (sesion != null) && (sesion.getRol() == Rol.ADMIN || sesion.getRol() == Rol.TECNICO);

        // Si usas secuencia/trigger para ID_REPORTE, podrías:
        //   txtIdReporte.setEditable(false);
        txtIdReporte.setEditable(true);

        cbLote.setEnabled(puede);
        cbPlaga.setEnabled(puede);
        cbSeveridad.setEnabled(puede);
        txtPorcentaje.setEditable(puede);
        txtObs.setEditable(puede);

        if (!puede) {
            JOptionPane.showMessageDialog(this,
                    "No tienes permiso para crear reportes (solo ADMIN o TÉCNICO).",
                    "Permisos", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ===================== Carga y lógica UI =====================

    private void cargarLotes() {
        cbLote.removeAllItems();
        try {
            List<Lote> lotes = control.lotesVisibles(sesion);
            for (Lote l : lotes) cbLote.addItem(l.getIdLote());
            if (cbLote.getItemCount() > 0) {
                cbLote.setSelectedIndex(0);
                onCambioLote();
            } else {
                cbPlaga.removeAllItems();
                txtIdCultivo.setText("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los lotes: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCambioLote() {
        String idLoteSel = (String) cbLote.getSelectedItem();
        if (idLoteSel == null || idLoteSel.isBlank()) {
            txtIdCultivo.setText("");
            cbPlaga.removeAllItems();
            return;
        }

        try {
            Lote l = loteDAO.buscarPorId(idLoteSel);
            String idCultivo = (l != null ? l.getIdCultivo() : null);
            txtIdCultivo.setText(idCultivo != null ? idCultivo : "");

            cbPlaga.removeAllItems();
            if (idCultivo != null && !idCultivo.isBlank()) {
                List<String> idsPlagas = cultivoPlagaDAO.plagasPermitidas(idCultivo);
                for (String pid : idsPlagas) cbPlaga.addItem(pid);
                if (cbPlaga.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(this,
                            "El cultivo del lote no tiene plagas asociadas en CULTIVO_PLAGA.",
                            "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo obtener datos del lote: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        // Mantiene selección de lote; limpia campos editables
        txtIdReporte.setText("");
        cbSeveridad.setSelectedIndex(0);
        txtPorcentaje.setText("");
        txtObs.setText("");
        txtIdReporte.requestFocusInWindow();
    }

    // ===================== Guardar =====================

    private boolean validarCampos() {
        if (txtIdReporte.isEditable() && txtIdReporte.getText().isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "ID Reporte es obligatorio (o configúralo con secuencia/trigger y desactiva este campo).",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            txtIdReporte.requestFocusInWindow();
            return false;
        }
        if (cbLote.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un lote.", "Validación", JOptionPane.WARNING_MESSAGE);
            cbLote.requestFocusInWindow();
            return false;
        }
        if (cbPlaga.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una plaga.", "Validación", JOptionPane.WARNING_MESSAGE);
            cbPlaga.requestFocusInWindow();
            return false;
        }
        if (!txtPorcentaje.getText().isBlank()) {
            try {
                double p = Double.parseDouble(txtPorcentaje.getText().trim());
                if (p < 0 || p > 100) {
                    JOptionPane.showMessageDialog(this, "El porcentaje debe estar entre 0 y 100.",
                            "Validación", JOptionPane.WARNING_MESSAGE);
                    txtPorcentaje.requestFocusInWindow();
                    return false;
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Porcentaje inválido.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                txtPorcentaje.requestFocusInWindow();
                return false;
            }
        }
        return true;
    }

    private void guardar() {
        if (!(sesion != null && (sesion.getRol() == Rol.ADMIN || sesion.getRol() == Rol.TECNICO))) {
            JOptionPane.showMessageDialog(this, "No tienes permisos para guardar.",
                    "Permisos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarCampos()) return;

        String idReporte = txtIdReporte.getText().trim();
        String idLote = (String) cbLote.getSelectedItem();
        String idPlaga = (String) cbPlaga.getSelectedItem();

        ReportePlaga r = new ReportePlaga();
        r.setIdReporte(idReporte);
        r.setIdLote(idLote);
        r.setIdPlaga(idPlaga);
        r.setSeveridad((String) cbSeveridad.getSelectedItem());
        r.setPorcentaje(txtPorcentaje.getText().isBlank()
                ? null
                : Double.parseDouble(txtPorcentaje.getText().trim()));
        r.setObservaciones(txtObs.getText().trim());
        r.setFechaReporte(Date.valueOf(LocalDate.now()));

        try {
            boolean ok = control.crear(sesion, r); // <== usa el controlador actualizado
            if (ok) {
                JOptionPane.showMessageDialog(this, "Reporte creado correctamente.");
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo crear el reporte: verifique asignación y relación cultivo-plaga.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
