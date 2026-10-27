package es.inf.uva.dis.interfaz.vistaycontrol.menu;

/**
 *
 * @author mgonzal
 */
public class MenuVista extends javax.swing.JFrame {
    
    private MenuControlador controladorVista; 
    
    public MenuVista() {
        initComponents();  
        controladorVista = new MenuControlador(this);
    }
    
    // Cambios de vista hacia el menu del empleado, cada uno adaptado a su rol
    public void setMenuCoordinador(){
        setTitle("Menú - Coordinador CallCenter");
        casoUso1Button.setText("Atender llamada");
        casoUso2Button.setText("Añadir servicio a un contrato");
        menuLabel.setText("Menu Coordinador");
    }
    public void setMenuSupervisor(){       
        setTitle("Menú - Supervisor de operaciones");
        casoUso1Button.setText("Consultar incidencias");
        casoUso2Button.setText("asignar técnico a base");
        menuLabel.setText("Menu Supervisor");
    }
    public void setMenuTecnico(){        
        setTitle("Menú - Técnico");
        casoUso1Button.setText("Cambiar disponibilidad al llegar a base");
        casoUso2Button.setText("Completar informe adjunto al parte");
        menuLabel.setText("Menu Tecnico");
    }

    @SuppressWarnings("unchecked")
        // ===== COMPONENTES INTERFAZ =====                            
    private void initComponents() {

        menuPanel = new javax.swing.JPanel();
        tituloPanel = new javax.swing.JPanel();
        casoUso1Button = new javax.swing.JButton();
        casoUso2Button = new javax.swing.JButton();
        cerrarSesionButton = new javax.swing.JButton();
        funcionalidadPanel = new javax.swing.JPanel();
        menuLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(483, 234));
        setMinimumSize(new java.awt.Dimension(675, 400));

        menuPanel.setLayout(new java.awt.BorderLayout());

        tituloPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        casoUso1Button.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        casoUso1Button.setText("casoUso1");
        casoUso1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                casoUso1ButtonActionPerformed(evt);
            }
        });

        casoUso2Button.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        casoUso2Button.setText("casoUso2");
        casoUso2Button.setEnabled(false);
        casoUso2Button.setFocusable(false);

        cerrarSesionButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cerrarSesionButton.setText("Cerrar Sesión");
        cerrarSesionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarSesionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tituloPanelLayout = new javax.swing.GroupLayout(tituloPanel);
        tituloPanel.setLayout(tituloPanelLayout);
        tituloPanelLayout.setHorizontalGroup(
            tituloPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tituloPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cerrarSesionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
            .addGroup(tituloPanelLayout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addGroup(tituloPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(casoUso1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(casoUso2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(144, Short.MAX_VALUE))
        );
        tituloPanelLayout.setVerticalGroup(
            tituloPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tituloPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(casoUso1Button, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(casoUso2Button, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(cerrarSesionButton)
                .addGap(18, 18, 18))
        );

        menuPanel.add(tituloPanel, java.awt.BorderLayout.CENTER);

        funcionalidadPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        menuLabel.setFont(new java.awt.Font("Sylfaen", 1, 36)); // NOI18N
        menuLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        menuLabel.setText("Menu");
        menuLabel.setAlignmentY(0.0F);

        javax.swing.GroupLayout funcionalidadPanelLayout = new javax.swing.GroupLayout(funcionalidadPanel);
        funcionalidadPanel.setLayout(funcionalidadPanelLayout);
        funcionalidadPanelLayout.setHorizontalGroup(
            funcionalidadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(funcionalidadPanelLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(menuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(87, Short.MAX_VALUE))
        );
        funcionalidadPanelLayout.setVerticalGroup(
            funcionalidadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, funcionalidadPanelLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(menuLabel)
                .addContainerGap())
        );

        menuPanel.add(funcionalidadPanel, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(menuPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>                        
     
    
    private void cerrarSesionButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controladorVista.cerrarSesion();
    }
    
    private void casoUso1ButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        controladorVista.entrarEnCasoUso();
    }  

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(() -> new MenuVista().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton casoUso1Button;
    private javax.swing.JButton casoUso2Button;
    private javax.swing.JButton cerrarSesionButton;
    private javax.swing.JPanel funcionalidadPanel;
    private javax.swing.JLabel menuLabel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JPanel tituloPanel;
    // End of variables declaration                   
}
