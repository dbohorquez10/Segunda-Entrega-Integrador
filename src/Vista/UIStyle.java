/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Utilidades de estilo para la UI (Swing).
 * MVC: esta clase se usa desde la capa "Vista".
 *
 * Objetivo:
 *  - Un tema verde consistente, legible y agradable.
 *  - Botones con buen contraste (texto blanco).
 *  - Encabezados de sección reutilizables.
 *  - Tarjetas de menú "clickables" (menuCard) con hover y cursor de mano.
 *
 * Notas:
 *  - Se usa sólo Swing puro para máxima compatibilidad con NetBeans.
 */

package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class UIStyle {

    // ===================== Paleta =====================
    // Verde principal (botones primarios y acentos)
    public static final Color GREEN = new Color(0x2e7d32);     // #2E7D32
    public static final Color GREEN_HOVER = new Color(0x276c2b);
    public static final Color GREEN_SOFT = new Color(0xe8f5e9); // fondo suave

    // Texto sobre botón oscuro
    public static final Color ON_PRIMARY = Color.WHITE;

    // Botón de peligro (eliminar)
    public static final Color DANGER = new Color(0xc62828);       // #C62828
    public static final Color DANGER_HOVER = new Color(0xab2222); // hover

    // Texto general
    public static final Color TEXT = new Color(0x263238);      // gris oscuro azulado
    public static final Color SUBTEXT = new Color(0x546e7a);   // gris medio

    private UIStyle() {}

    // ===================== Tema global =====================

    public static void applyGlobalTheme() {
        // Fuentes
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TextArea.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));

        // Colores básicos
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 13));
    }

    // ===================== Encabezado de sección =====================

    /*
     * Crea un encabezado de sección consistente con título y subtítulo.
     * Puedes agregar botones (logout/back) al panel retornado con .add().
     */
    public static JPanel sectionHeader(String title, String subtitle) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(true);
        p.setBackground(GREEN_SOFT);
        p.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel h = new JLabel(title);
        h.setFont(new Font("Segoe UI", Font.BOLD, 20));
        h.setForeground(GREEN.darker());

        JLabel s = new JLabel(subtitle);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        s.setForeground(SUBTEXT);

        p.add(h);
        p.add(Box.createVerticalStrut(2));
        p.add(s);
        return p;
    }

    // ===================== Botones =====================

    public static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        styleAsPrimary(b);
        return b;
    }

    public static JButton dangerButton(String text) {
        JButton b = new JButton(text);
        styleAsDanger(b);
        return b;
    }

    public static JButton backButton() {
        JButton b = new JButton("Volver");
        // Botón contorneado verde
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        b.setForeground(GREEN.darker());
        b.setBorder(BorderFactory.createLineBorder(GREEN));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Hover
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                b.setBackground(new Color(0xf3fbf4));
            }
            @Override public void mouseExited(MouseEvent e) {
                b.setBackground(Color.WHITE);
            }
        });
        return b;
    }

    private static void styleAsPrimary(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(GREEN);
        b.setForeground(ON_PRIMARY);     // ¡texto blanco para que se vea!
        b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(GREEN_HOVER); }
            @Override public void mouseExited(MouseEvent e)  { b.setBackground(GREEN); }
        });
    }

    private static void styleAsDanger(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(DANGER);
        b.setForeground(ON_PRIMARY);
        b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(DANGER_HOVER); }
            @Override public void mouseExited(MouseEvent e)  { b.setBackground(DANGER); }
        });
    }

    // ===================== Tarjetas de menú =====================

    /*
     * Tarjeta de menú "clickable" con título y subtítulo.
     * - Fondo blanco, borde suave, sombra simulada con línea.
     * - Hover: fondo verde muy suave y borde verde.
     * - Cursor de mano; el click se maneja externamente con addMouseListener en quien la usa.
     *
     * Uso:
     *   JPanel card = UIStyle.menuCard("Cultivos", "Crear/editar cultivos");
     *   card.addMouseListener(new MouseAdapter(){ public void mouseClicked(MouseEvent e){ ... }});
     */
    public static JPanel menuCard(String title, String subtitle) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(8, 8));
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE0E0E0)),
                new EmptyBorder(16, 16, 16, 16)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel h = new JLabel(title);
        h.setFont(new Font("Segoe UI", Font.BOLD, 16));
        h.setForeground(TEXT);

        JLabel s = new JLabel("<html><div style='width:260px;'>" + subtitle + "</div></html>");
        s.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        s.setForeground(SUBTEXT);

        card.add(h, BorderLayout.NORTH);
        card.add(s, BorderLayout.CENTER);

        // Hover
        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                card.setBackground(GREEN_SOFT);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GREEN),
                        new EmptyBorder(16, 16, 16, 16)
                ));
            }
            @Override public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0xE0E0E0)),
                        new EmptyBorder(16, 16, 16, 16)
                ));
            }
        });

        return card;
    }
}
