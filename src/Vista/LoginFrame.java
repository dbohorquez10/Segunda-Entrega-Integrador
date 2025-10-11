/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Ventana de inicio de sesión (Swing).
 * MVC: esta clase pertenece a la capa "Vista".
 *
 * Flujo:
 *  - Usuario ingresa credenciales.
 *  - Usa AuthControl (controlador) para validar.
 *  - Si el login es correcto, abre MenuPrincipalFrame.
 */
package vista;

import controlador.AuthControl;
import modelo.Sesion;
import modelo.ConexionBD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private final AuthControl auth = new AuthControl();

    public LoginFrame() {
        UIStyle.applyGlobalTheme();
        setTitle("ICA-FITO · Iniciar sesión");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(16,16,16,16));

        JPanel header = UIStyle.sectionHeader(
                "Bienvenido",
                "Inicia sesión para gestionar cultivos, plagas y reportes"
        );

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(16,16,8,16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtUser = new JTextField(18);
        txtPass = new JPasswordField(18);

        int y=0;
        c.gridx=0; c.gridy=y; form.add(new JLabel("Usuario"), c); c.gridx=1; form.add(txtUser, c);
        y++; c.gridx=0; c.gridy=y; form.add(new JLabel("Contraseña"), c); c.gridx=1; form.add(txtPass, c);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCon = UIStyle.primaryButton("Ingresar");
        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> System.exit(0));
        actions.add(btnSalir); actions.add(btnCon);

        btnCon.addActionListener(e -> onLogin());

        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(actions, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void onLogin() {
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword());
        try {
            // Verifica driver/conexión temprano para dar error claro si falta ojdbc
            ConexionBD.getConnection();
            Sesion s = auth.login(u, p);
            new MainAppFrame(s).setVisible(true);
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}