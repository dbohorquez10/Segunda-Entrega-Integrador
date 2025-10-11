/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Ventana principal (Swing).
 * MVC: esta clase pertenece a la capa "Vista".
 *
 * Objetivo:
 *  - Una sola ventana con CardLayout para navegar entre módulos.
 *  - Menú inicial tipo “grid” de tarjetas.
 *  - Botón “Cerrar sesión” que vuelve al Login sin cerrar NetBeans.
 *  - Acceso a módulos según el rol:
 *      · ADMIN: Cultivos, Plagas, Reporte, Lotes, Asignaciones.
 *      · TÉCNICO: Cultivos(lectura), Plagas(lectura), Reporte, Lotes(editar), sin Asignaciones.
 *      · PRODUCTOR: (si lo usas más adelante).
 *
 * Notas:
 *  - Asume la existencia de UIStyle, Refreshable y los paneles: CultivoPanel, PlagaPanel,
 *    ReportePlagaPanel, LotePanel, AsignacionLotePanel. Todos reciben (Sesion, onBack).
 *  - El botón Cerrar sesión hace dispose() y abre LoginFrame().
 */

package vista;

import modelo.Rol;
import modelo.Sesion;

import javax.swing.*;
import java.awt.*;

public class MainAppFrame extends JFrame {

    // ---- tarjetas
    public static final String CARD_MENU   = "MENU";
    public static final String CARD_CULT   = "CULTIVOS";
    public static final String CARD_PLAGA  = "PLAGAS";
    public static final String CARD_REP    = "REPORTE";
    public static final String CARD_LOTES  = "LOTES";
    public static final String CARD_ASIG   = "ASIGNACIONES";

    private final Sesion sesion;

    private JPanel root;           // contenedor con CardLayout
    private CardLayout cards;

    // menú
    private JPanel menuGrid;

    // módulos
    private CultivoPanel cultivoPanel;
    private PlagaPanel plagaPanel;
    private ReportePlagaPanel reportePanel;
    private LotePanel lotePanel;
    private AsignacionLotePanel asigPanel;

    public MainAppFrame(Sesion sesion) {
        this.sesion = sesion;
        UIStyle.applyGlobalTheme();
        setTitle("ICA-FITO · Panel principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        initUI();
    }

    // ===================== UI =====================

    private void initUI() {
        cards = new CardLayout();
        root = new JPanel(cards);

        // ---- Header global con botón Cerrar sesión
        JPanel header = UIStyle.sectionHeader(
                "Panel principal",
                "Usuario: " + sesion.getUsername() + " · Rol: " + sesion.getRol()
        );
        JButton btnLogout = UIStyle.primaryButton("Cerrar sesión");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        header.add(Box.createVerticalStrut(8));
        header.add(btnLogout);

        // ---- menú tipo grid
        menuGrid = buildMenu();

        // ---- paneles de módulos
        cultivoPanel = new CultivoPanel(sesion, this::goMenu);
        plagaPanel = new PlagaPanel(sesion, this::goMenu);
        reportePanel = new ReportePlagaPanel(sesion, this::goMenu);
        lotePanel = new LotePanel(sesion, this::goMenu);
        asigPanel = new AsignacionLotePanel(sesion, this::goMenu);

        JPanel menuCard = new JPanel(new BorderLayout(12,12));
        menuCard.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        menuCard.add(header, BorderLayout.NORTH);
        menuCard.add(menuGrid, BorderLayout.CENTER);

        root.add(menuCard, CARD_MENU);
        root.add(wrapWithHeader(CARD_CULT, "Cultivos", "Gestión de cultivos.", cultivoPanel), CARD_CULT);
        root.add(wrapWithHeader(CARD_PLAGA, "Plagas", "Gestión de plagas.", plagaPanel), CARD_PLAGA);
        root.add(wrapWithHeader(CARD_REP, "Reporte de plaga", "Crear reportes sobre lotes asignados.", reportePanel), CARD_REP);
        root.add(wrapWithHeader(CARD_LOTES, "Lotes", "Crear/actualizar lotes.", lotePanel), CARD_LOTES);
        root.add(wrapWithHeader(CARD_ASIG, "Asignaciones", "Asignar lotes a técnicos.", asigPanel), CARD_ASIG);

        setContentPane(root);
        cards.show(root, CARD_MENU);
    }

    private JPanel wrapWithHeader(String card, String title, String subtitle, JComponent body) {
        JPanel p = new JPanel(new BorderLayout(12,12));
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        JPanel h = UIStyle.sectionHeader(title, subtitle);

        // botón volver al menú
        JButton btnBack = UIStyle.backButton();
        btnBack.addActionListener(e -> goMenu());
        h.add(Box.createVerticalStrut(8));
        h.add(btnBack);

        // botón cerrar sesión visible en todas las tarjetas
        JButton btnLogout = UIStyle.primaryButton("Cerrar sesión");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        h.add(Box.createVerticalStrut(8));
        h.add(btnLogout);

        p.add(h, BorderLayout.NORTH);
        p.add(body, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildMenu() {
        JPanel grid = new JPanel(new GridLayout(0, 3, 12, 12)); // 3 columnas
        grid.setOpaque(false);

        // Tarjetas comunes
        grid.add(card("Cultivos", "Crear/editar cultivos (ADMIN).", () -> {
            safeRefresh(cultivoPanel);
            cards.show(root, CARD_CULT);
        }));
        grid.add(card("Plagas", "Crear/editar plagas (ADMIN).", () -> {
            safeRefresh(plagaPanel);
            cards.show(root, CARD_PLAGA);
        }));
        grid.add(card("Reporte de plaga", "Registrar eventos sobre lotes asignados.", () -> {
            safeRefresh(reportePanel);
            cards.show(root, CARD_REP);
        }));

        // Lotes (ADMIN: crear/editar; TECNICO: editar)
        grid.add(card("Lotes", "Crear/actualizar lotes.", () -> {
            safeRefresh(lotePanel);
            cards.show(root, CARD_LOTES);
        }));

        // Asignaciones (solo ADMIN)
        JPanel asig = card("Asignaciones", "Asignar lotes a técnicos (solo admin).", () -> {
            if (sesion.getRol() != Rol.ADMIN) {
                JOptionPane.showMessageDialog(this, "Solo ADMIN puede abrir Asignaciones.");
                return;
            }
            safeRefresh(asigPanel);
            cards.show(root, CARD_ASIG);
        });
        if (sesion.getRol() != Rol.ADMIN) {
            asig.setEnabled(false);
        }
        grid.add(asig);

        // Cerrar sesión directo desde menú (atajo)
        grid.add(card("Cerrar sesión", "Volver a la pantalla de inicio de sesión.", () -> {
            dispose();
            new LoginFrame().setVisible(true);
        }));

        return grid;
    }

    // Tarjeta “bonita” usando UIStyle
    private JPanel card(String title, String subtitle, Runnable onClick) {
        JPanel card = UIStyle.menuCard(title, subtitle);
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { onClick.run(); }
        });
        return card;
    }

    private void safeRefresh(Object panel) {
        if (panel instanceof Refreshable r) {
            r.refresh();
        }
    }

    private void goMenu() {
        cards.show(root, CARD_MENU);
    }
}
