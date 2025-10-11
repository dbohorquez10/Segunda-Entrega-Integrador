/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Asignación de Lotes a Técnicos (solo ADMIN).
 * - Combo de técnico (USERNAMES con rol TECNICO).
 * - Combo de lote (todos los lotes).
 * - Botones Asignar / Desasignar / Desasignar Todo.
 */
package vista;

import controlador.AsignacionLoteControl;
import modelo.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class AsignacionLotePanel extends JPanel implements Refreshable {

    private final Sesion sesion;
    private final Runnable onBack;
    private final AsignacionLoteControl control = new AsignacionLoteControl();
    private final LoteDAO loteDAO = new LoteDAO();

    private JComboBox<String> cbTecnico;
    private JComboBox<String> cbLote;
    private JButton btnAsignar, btnDesasignar, btnDesasignarTodo;

    public AsignacionLotePanel(Sesion sesion, Runnable onBack) {
        this.sesion = sesion;
        this.onBack = onBack;
        buildUI();
        aplicarPermisos();
        cargarCombos();
    }

    @Override public void refresh() { cargarCombos(); }

    private void buildUI() {
        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JPanel header = UIStyle.sectionHeader("Asignación de lotes", "Asigna o quita lotes a técnicos.");
        JButton btnBack = UIStyle.backButton();
        btnBack.addActionListener(e -> onBack.run());
        header.add(Box.createVerticalStrut(8));
        header.add(btnBack);
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new TitledBorder("Acciones"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        cbTecnico = new JComboBox<>();
        cbLote = new JComboBox<>();

        int y=0;
        c.gridx=0; c.gridy=y; form.add(new JLabel("Técnico (username)"), c);
        c.gridx=1;           form.add(cbTecnico, c); y++;

        c.gridx=0; c.gridy=y; form.add(new JLabel("Lote"), c);
        c.gridx=1;            form.add(cbLote, c); y++;

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAsignar = UIStyle.primaryButton("Asignar");
        btnDesasignar = new JButton("Desasignar");
        btnDesasignarTodo = UIStyle.dangerButton("Desasignar todo (lote)");
        acciones.add(btnDesasignar);
        acciones.add(btnDesasignarTodo);
        acciones.add(btnAsignar);

        c.gridx=0; c.gridy=y; c.gridwidth=2; form.add(acciones, c);

        add(form, BorderLayout.CENTER);

        btnAsignar.addActionListener(e -> asignar());
        btnDesasignar.addActionListener(e -> desasignar());
        btnDesasignarTodo.addActionListener(e -> desasignarTodo());
    }

    private void aplicarPermisos() {
        if (sesion.getRol() != Rol.ADMIN) {
            cbTecnico.setEnabled(false);
            cbLote.setEnabled(false);
            btnAsignar.setEnabled(false);
            btnDesasignar.setEnabled(false);
            btnDesasignarTodo.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Solo ADMIN puede asignar/desasignar lotes.",
                    "Permisos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarCombos() {
        cbTecnico.removeAllItems();
        cbLote.removeAllItems();
        try {
            for (String u : control.listarTecnicos()) cbTecnico.addItem(u);
            for (Lote l : loteDAO.listarTodos()) cbLote.addItem(l.getIdLote());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void asignar() {
        String u = (String) cbTecnico.getSelectedItem();
        String l = (String) cbLote.getSelectedItem();
        if (u == null || l == null) { JOptionPane.showMessageDialog(this, "Seleccione técnico y lote."); return; }
        try {
            boolean ok = control.asignar(sesion, u, l);
            JOptionPane.showMessageDialog(this, ok ? "Asignación creada." : "No se pudo asignar.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Permisos", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desasignar() {
        String u = (String) cbTecnico.getSelectedItem();
        String l = (String) cbLote.getSelectedItem();
        if (u == null || l == null) { JOptionPane.showMessageDialog(this, "Seleccione técnico y lote."); return; }
        try {
            boolean ok = control.desasignar(sesion, u, l);
            JOptionPane.showMessageDialog(this, ok ? "Asignación eliminada." : "No hubo cambios.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Permisos", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desasignarTodo() {
        String l = (String) cbLote.getSelectedItem();
        if (l == null) { JOptionPane.showMessageDialog(this, "Seleccione un lote."); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Quitar TODAS las asignaciones del lote " + l + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            boolean ok = control.desasignarTodo(sesion, l);
            JOptionPane.showMessageDialog(this, ok ? "Se eliminaron asignaciones." : "No había asignaciones.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Permisos", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

