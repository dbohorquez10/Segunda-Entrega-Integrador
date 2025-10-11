/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Gestión de Lotes:
 * - ADMIN: crear, actualizar, eliminar.
 * - TÉCNICO: actualizar (no crear, no eliminar).
 * Diseño verde y en una sola pantalla.
 */
package vista;

import controlador.LoteControl;
import modelo.Lote;
import modelo.Rol;
import modelo.Sesion;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LotePanel extends JPanel implements Refreshable {

    private final Sesion sesion;
    private final Runnable onBack;
    private final LoteControl control = new LoteControl();

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtCultivo, txtArea;
    private JButton btnNuevo, btnGuardar, btnEliminar, btnRefrescar;

    public LotePanel(Sesion sesion, Runnable onBack) {
        this.sesion = sesion;
        this.onBack = onBack;
        buildUI();
        aplicarPermisos();
        cargarTabla();
    }

    @Override public void refresh() { cargarTabla(); }

    private void buildUI() {
        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JPanel header = UIStyle.sectionHeader("Lotes", "Crea y actualiza lotes.");
        JButton btnBack = UIStyle.backButton();
        btnBack.addActionListener(e -> onBack.run());
        header.add(Box.createVerticalStrut(8));
        header.add(btnBack);

        modeloTabla = new DefaultTableModel(new Object[]{"ID_LOTE","ID_CULTIVO","AREA"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(new TitledBorder("Listado"));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new TitledBorder("Detalle"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(14);
        txtCultivo = new JTextField(16);
        txtArea = new JTextField(10);

        int y=0;
        c.gridx=0; c.gridy=y; form.add(new JLabel("ID Lote"), c); c.gridx=1; form.add(txtId, c);
        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("ID Cultivo"), c); c.gridx=1; form.add(txtCultivo, c);
        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Área"), c); c.gridx=1; form.add(txtArea, c);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRefrescar = new JButton("Refrescar");
        btnNuevo = UIStyle.primaryButton("Nuevo");
        btnGuardar = UIStyle.primaryButton("Guardar");
        btnEliminar = UIStyle.dangerButton("Eliminar");
        acciones.add(btnRefrescar); acciones.add(btnNuevo); acciones.add(btnGuardar); acciones.add(btnEliminar);

        y++; c.gridx=0; c.gridy=y; c.gridwidth=2; form.add(acciones, c);

        add(header, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        add(form, BorderLayout.EAST);

        // eventos
        btnRefrescar.addActionListener(e -> cargarTabla());
        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        tabla.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) onSelect(); });
    }

    private void aplicarPermisos() {
        boolean admin = sesion.getRol() == Rol.ADMIN;
        // Admin todo
        txtId.setEnabled(admin);  // solo admin crea, técnico no toca el ID
        btnNuevo.setEnabled(admin);
        btnEliminar.setEnabled(admin);
        // Guardar habilitado para ambos (admin crea/actualiza, técnico solo actualiza)
        btnGuardar.setEnabled(true);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<Lote> lista = control.listarTodos();
            for (Lote l : lista) {
                modeloTabla.addRow(new Object[]{ l.getIdLote(), l.getIdCultivo(), l.getArea() });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando lotes: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSelect() {
        int r = tabla.getSelectedRow();
        if (r < 0) return;
        txtId.setText(String.valueOf(modeloTabla.getValueAt(r, 0)));
        txtCultivo.setText(String.valueOf(modeloTabla.getValueAt(r, 1)));
        txtArea.setText(String.valueOf(modeloTabla.getValueAt(r, 2)));
    }

    private void limpiar() {
        txtId.setText("");
        txtCultivo.setText("");
        txtArea.setText("");
        tabla.clearSelection();
        txtId.requestFocusInWindow();
    }

    private boolean validar() {
        if (txtCultivo.getText().isBlank()) {
            warn("El ID de cultivo es obligatorio.", txtCultivo); return false;
        }
        return true;
    }

    private void guardar() {
        if (!validar()) return;
        Lote l = new Lote(txtId.getText().trim(), txtCultivo.getText().trim(), txtArea.getText().trim());

        try {
            boolean admin = sesion.getRol() == Rol.ADMIN;
            boolean crear = admin && txtId.getText().trim().length() > 0 && (tabla.getSelectedRow() < 0);

            boolean ok = crear ? control.crear(sesion, l) : control.actualizar(sesion, l);
            if (ok) {
                JOptionPane.showMessageDialog(this, crear ? "Lote creado." : "Lote actualizado.");
                cargarTabla(); limpiar();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Permisos/Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error guardando: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int r = tabla.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Seleccione un lote."); return; }
        String id = String.valueOf(modeloTabla.getValueAt(r, 0));
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar lote " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION)
                != JOptionPane.YES_OPTION) return;
        try {
            boolean ok = control.eliminar(sesion, id);
            if (ok) { JOptionPane.showMessageDialog(this, "Lote eliminado."); cargarTabla(); limpiar(); }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Permisos/Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void warn(String m, JComponent c) {
        JOptionPane.showMessageDialog(this, m, "Validación", JOptionPane.WARNING_MESSAGE);
        c.requestFocusInWindow();
    }
}