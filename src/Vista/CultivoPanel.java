/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Pantalla CRUD de Cultivos (Swing).
 * MVC: Vista.
 *
 * Reglas por rol:
 *  - ADMIN: crear, editar y eliminar.
 *  - TECNICO / PRODUCTOR: solo lectura (botones deshabilitados).
 */
package vista;

import controlador.CultivoControl;
import modelo.Cultivo;
import modelo.Rol;
import modelo.Sesion;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CultivoPanel extends JPanel implements Refreshable {

    private final Sesion sesion;
    private final Runnable onBack;
    private final CultivoControl control = new CultivoControl();

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtNomCientifico, txtNomComun, txtCiclo;
    private JButton btnNuevo, btnGuardar, btnEliminar, btnRefrescar;

    public CultivoPanel(Sesion sesion, Runnable onBack) {
        this.sesion = sesion;
        this.onBack = onBack;
        buildUI();
        aplicarPermisos();
        cargarTabla();
    }

    @Override
    public void refresh() { cargarTabla(); }

    private void buildUI() {
        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JPanel header = UIStyle.sectionHeader("Cultivos", "Administra el catálogo de cultivos.");
        JButton btnBack = UIStyle.backButton();
        btnBack.addActionListener(e -> onBack.run());
        header.add(Box.createVerticalStrut(8));
        header.add(btnBack);

        modeloTabla = new DefaultTableModel(new Object[]{"ID","Nombre científico","Nombre común","Ciclo"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        JScrollPane spTabla = new JScrollPane(tabla);
        spTabla.setBorder(new TitledBorder("Listado"));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new TitledBorder("Detalle"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(12);
        txtNomCientifico = new JTextField(22);
        txtNomComun = new JTextField(22);
        txtCiclo = new JTextField(14);

        int y=0;
        c.gridx=0; c.gridy=y; form.add(new JLabel("ID"), c); c.gridx=1; form.add(txtId, c);
        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Nombre científico"), c); c.gridx=1; form.add(txtNomCientifico, c);
        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Nombre común"), c); c.gridx=1; form.add(txtNomComun, c);
        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Ciclo"), c); c.gridx=1; form.add(txtCiclo, c);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRefrescar = new JButton("Refrescar");
        btnNuevo = UIStyle.primaryButton("Nuevo");
        btnEliminar = UIStyle.dangerButton("Eliminar");
        btnGuardar = UIStyle.primaryButton("Guardar");
        acciones.add(btnRefrescar); acciones.add(btnNuevo); acciones.add(btnEliminar); acciones.add(btnGuardar);

        y++; c.gridx=0; c.gridy=y; c.gridwidth=2; form.add(acciones, c);

        tabla.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) onSeleccionFila(); });
        btnRefrescar.addActionListener(e -> cargarTabla());
        btnNuevo.addActionListener(e -> limpiarForm());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());

        add(header, BorderLayout.NORTH);
        add(spTabla, BorderLayout.CENTER);
        add(form, BorderLayout.EAST);
    }

    private void aplicarPermisos() {
        boolean esAdmin = (sesion != null && sesion.getRol() == Rol.ADMIN);
        txtId.setEnabled(esAdmin);
        txtNomCientifico.setEnabled(esAdmin);
        txtNomComun.setEnabled(esAdmin);
        txtCiclo.setEnabled(esAdmin);
        btnNuevo.setEnabled(esAdmin);
        btnGuardar.setEnabled(esAdmin);
        btnEliminar.setEnabled(esAdmin);
        btnRefrescar.setEnabled(true);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<Cultivo> lista = control.listar();
            for (Cultivo cu : lista) {
                modeloTabla.addRow(new Object[]{ cu.getIdCultivo(), cu.getNombreCientifico(), cu.getNombreComun(), cu.getCicloCultivo() });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando cultivos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSeleccionFila() {
        int row = tabla.getSelectedRow();
        if (row < 0) return;
        txtId.setText(String.valueOf(modeloTabla.getValueAt(row, 0)));
        txtNomCientifico.setText(String.valueOf(modeloTabla.getValueAt(row, 1)));
        txtNomComun.setText(String.valueOf(modeloTabla.getValueAt(row, 2)));
        txtCiclo.setText(String.valueOf(modeloTabla.getValueAt(row, 3)));
    }

    private void limpiarForm() {
        txtId.setText(""); txtNomCientifico.setText(""); txtNomComun.setText(""); txtCiclo.setText("");
        tabla.clearSelection(); txtId.requestFocusInWindow();
    }

    private boolean validar() {
        if (txtId.getText().isBlank()) { warn("El ID es obligatorio.", txtId); return false; }
        if (txtNomCientifico.getText().isBlank()) { warn("El nombre científico es obligatorio.", txtNomCientifico); return false; }
        if (txtNomComun.getText().isBlank()) { warn("El nombre común es obligatorio.", txtNomComun); return false; }
        return true;
    }

    private void guardar() {
    if (!validar()) return;

    Cultivo cu = new Cultivo(
            txtId.getText().trim(),
            txtNomCientifico.getText().trim(),
            txtNomComun.getText().trim(),
            txtCiclo.getText().trim()
    );

    try {
        String res = control.guardar(sesion, cu);  // "CREADO" o "ACTUALIZADO"
        JOptionPane.showMessageDialog(this,
                res.equals("CREADO") ? "Cultivo creado." : "Cultivo actualizado.");
        cargarTabla();
        limpiarForm();
    } catch (IllegalArgumentException | IllegalStateException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Permisos/Validación", JOptionPane.WARNING_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error guardando: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void eliminar() {
        int row = tabla.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Seleccione un registro."); return; }
        String id = String.valueOf(modeloTabla.getValueAt(row, 0));
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar cultivo " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            boolean ok = control.eliminar(sesion, id);
            if (ok) { JOptionPane.showMessageDialog(this, "Cultivo eliminado."); cargarTabla(); limpiarForm(); }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Permisos/Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void warn(String msg, JComponent comp) {
        JOptionPane.showMessageDialog(this, msg, "Validación", JOptionPane.WARNING_MESSAGE);
        comp.requestFocusInWindow();
    }
}
