package es.inf.uva.dis.interfaz.vistaycontrol.cambiardisponibilidad;

import java.awt.CardLayout;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

/**
 *
 * @author mgonzal
 */
public class CambiarDisponibilidadVista extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CambiarDisponibilidadVista.class.getName());
    private CambiarDisponibilidadControlador controladorVista;
    private CardLayout funcLayout;
    private SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss"); // Para el reloj
    
    
    public CambiarDisponibilidadVista() {
        initComponents();
        
        controladorVista = new CambiarDisponibilidadControlador(this);
        
        //Esto crea el Layout carta para poder cambiar entre el panel entrar 
        // en base y el de cambiar la disponibilidad.
        funcLayout = new CardLayout();
        funcionalidadPanel.setLayout(funcLayout);
        funcionalidadPanel.add(cambioDispPanel, "cambioDisp");
        funcionalidadPanel.add(entrarBasePanel, "entrarBase");
        funcLayout.show(funcionalidadPanel,"entrarBase");
        
        setLocationRelativeTo(null);
        setTitle("Cambiar disponibilidad al llegar a la base - Técnico");
        reloj();
    }
    
    // Se cambia la vista para que el usuario pueda 
    public void setVistaCambioDisponibilidad(String direccion){
        lugarLabel.setText("Direccion: " + direccion);
        funcLayout.show(funcionalidadPanel,"cambioDisp");
        limpiarError();
    }
    
    public String getNuevoEstado(){
        int estadoInt = estadoDisponibilidadComboBox.getSelectedIndex();
        String estado = null;
        if(estadoInt == 1)
            estado = "disponible";
        else if (estadoInt == 2)
            estado = "noDisponible";
        return estado;
    }
    
    // El usuario acciona boton entrar en base
    private void entrarBaseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controladorVista.entrarBaseEvento();    
    }
    
    // Acciona boton aceptar para cambiar su disponibilidad
    private void cambiarDispButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int estado = estadoDisponibilidadComboBox.getSelectedIndex();
        if ( estado == 0){
            mostrarErrorCambiarDisp("Debe seleccionar un estado");
        } else {
            // Solo llamamos al controlador y que él decida qué mostrar
            controladorVista.procesaEventoBotonAceptarCambioDisponibilidad();
        }
    }

    public void mostrarExito(String mensaje) {
        // Limpia y resetea el panel de cambio de disponibilidad
        estadoDisponibilidadComboBox.setSelectedIndex(0);
        lugarLabel.setText("Direccion: ");
        mensajeErrorLabel.setText("");

        // Vuelve al panel de "Entrar en base"
        funcLayout.show(funcionalidadPanel, "entrarBase");

        // Muestra el mensaje de éxito en ese panel
        mensajeErrorBaseLabel.setForeground(java.awt.Color.GREEN.darker());
        mensajeErrorBaseLabel.setText(mensaje);
    }
    
    public void mostrarErrorRN3(String error) {
        estadoDisponibilidadComboBox.setSelectedIndex(0);
        mensajeErrorLabel.setText("");
        funcLayout.show(funcionalidadPanel, "entrarBase");
        mensajeErrorBaseLabel.setForeground(java.awt.Color.RED);
        mensajeErrorBaseLabel.setText(error);
    }
    
    // Cancela su cambio de disponibilidad (desde cambioDispPanel → vuelve a entrarBasePanel)
    private void cancelarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        estadoDisponibilidadComboBox.setSelectedIndex(0);
        lugarLabel.setText("Direccion: ");
        limpiarError();
        funcLayout.show(funcionalidadPanel, "entrarBase");
        logger.info("El usuario ha cancelado la operación actual.");
    }

    // Cancela desde entrarBasePanel → vuelve al menú del técnico
    private void cancelarBaseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controladorVista.volverAlMenu();
    }

    public void cerrarVista() {
        this.setVisible(false);
        this.dispose();
    }

    
    // Manejo de errores en pantalla
    public void mostrarErrorEntrarBase(String error){
        mensajeErrorBaseLabel.setForeground(java.awt.Color.RED);
        mensajeErrorBaseLabel.setText(error);
    }
    public void mostrarErrorCambiarDisp(String error){
        mensajeErrorLabel.setForeground(java.awt.Color.RED);
        mensajeErrorLabel.setText(error);
    }
    private void limpiarError(){
        mensajeErrorBaseLabel.setText("");
        mensajeErrorLabel.setText("");
    }
    
    // Reloj para el tecnico
    private void reloj() {
        horaLabel.setText("Hora: " + hms.format(new Date()));
        
        // Cada segundo ejecuta el evento de cambiar el label a la hora actual.
        Timer timer = new Timer(1000, e -> 
                horaLabel.setText("Hora: " + hms.format(new Date())));
        timer.start();
    }
    
    @SuppressWarnings("unchecked")
    // ===== COMPONENTES INTERFAZ =====                          
    private void initComponents() {

        funcionalidadPanel = new javax.swing.JPanel();
        cambioDispPanel = new javax.swing.JPanel();
        estadoDisponibilidadComboBox = new javax.swing.JComboBox<>();
        estadoTecnicoLabel = new javax.swing.JLabel();
        cambiarDispButton = new javax.swing.JButton();
        mensajeErrorLabel = new javax.swing.JLabel();
        cancelarButton = new javax.swing.JButton();
        entrarBasePanel = new javax.swing.JPanel();
        entrarBaseButton = new javax.swing.JButton();
        mensajeErrorBaseLabel = new javax.swing.JLabel();
        tituloPanel = new javax.swing.JPanel();
        cambiarDisponibilidadTituloLabel = new javax.swing.JLabel();
        infoPanel = new javax.swing.JPanel();
        fechaLabel = new javax.swing.JLabel();
        horaLabel = new javax.swing.JLabel();
        lugarLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        funcionalidadPanel.setLayout(new java.awt.CardLayout());

        estadoDisponibilidadComboBox.setFont(new java.awt.Font("Sylfaen", 0, 18)); // NOI18N
        estadoDisponibilidadComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"", "Disponible", "No Disponible" }));

        estadoTecnicoLabel.setFont(new java.awt.Font("Sylfaen", 0, 18)); // NOI18N
        estadoTecnicoLabel.setText("Estado del Técnico:");

        cambiarDispButton.setFont(new java.awt.Font("Sylfaen", 0, 14)); // NOI18N
        cambiarDispButton.setText("Aceptar");
        cambiarDispButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarDispButtonActionPerformed(evt);
            }
        });

        cancelarButton.setFont(new java.awt.Font("Sylfaen", 0, 14)); // NOI18N
        cancelarButton.setText("Cancelar");
        cancelarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cambioDispPanelLayout = new javax.swing.GroupLayout(cambioDispPanel);
        cambioDispPanel.setLayout(cambioDispPanelLayout);
        cambioDispPanelLayout.setHorizontalGroup(
            cambioDispPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cambioDispPanelLayout.createSequentialGroup()
                .addGroup(cambioDispPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cambioDispPanelLayout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(estadoDisponibilidadComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cambioDispPanelLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(cambioDispPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cambioDispPanelLayout.createSequentialGroup()
                                .addComponent(cancelarButton)
                                .addGap(32, 32, 32)
                                .addComponent(mensajeErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(estadoTecnicoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(222, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cambioDispPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cambiarDispButton, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );
        cambioDispPanelLayout.setVerticalGroup(
            cambioDispPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cambioDispPanelLayout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(cambioDispPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cancelarButton)
                    .addGroup(cambioDispPanelLayout.createSequentialGroup()
                        .addComponent(estadoTecnicoLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(estadoDisponibilidadComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(cambioDispPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cambiarDispButton)
                            .addComponent(mensajeErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(25, 25, 25))
        );

        funcionalidadPanel.add(cambioDispPanel, "cambioDisp");

        entrarBaseButton.setFont(new java.awt.Font("Sylfaen", 0, 24)); // NOI18N
        entrarBaseButton.setText("Entrar en base");
        entrarBaseButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        entrarBaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entrarBaseButtonActionPerformed(evt);
            }
        });

        cancelarBaseButton = new javax.swing.JButton();
        cancelarBaseButton.setFont(new java.awt.Font("Sylfaen", 0, 14)); // NOI18N
        cancelarBaseButton.setText("Cancelar");
        cancelarBaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarBaseButtonActionPerformed(evt);
            }
        });

        mensajeErrorBaseLabel.setFont(new java.awt.Font("Sylfaen", 0, 18)); // NOI18N
        mensajeErrorBaseLabel.setText("");

        javax.swing.GroupLayout entrarBasePanelLayout = new javax.swing.GroupLayout(entrarBasePanel);
        entrarBasePanel.setLayout(entrarBasePanelLayout);
        entrarBasePanelLayout.setHorizontalGroup(
            entrarBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(entrarBasePanelLayout.createSequentialGroup()
                .addGroup(entrarBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(entrarBasePanelLayout.createSequentialGroup()
                        .addGap(295, 295, 295)
                        .addComponent(entrarBaseButton))
                    .addGroup(entrarBasePanelLayout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(mensajeErrorBaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(entrarBasePanelLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(cancelarBaseButton)))
                .addContainerGap(547, Short.MAX_VALUE))
        );
        entrarBasePanelLayout.setVerticalGroup(
            entrarBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(entrarBasePanelLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(entrarBaseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(mensajeErrorBaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(cancelarBaseButton)
                .addGap(17, 17, 17))
        );

        funcionalidadPanel.add(entrarBasePanel, "entrarBase");

        getContentPane().add(funcionalidadPanel, java.awt.BorderLayout.PAGE_END);

        tituloPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        cambiarDisponibilidadTituloLabel.setFont(new java.awt.Font("Sylfaen", 1, 36)); // NOI18N
        cambiarDisponibilidadTituloLabel.setText("Cambiar Disponibilidad al llegar a Base");

        javax.swing.GroupLayout tituloPanelLayout = new javax.swing.GroupLayout(tituloPanel);
        tituloPanel.setLayout(tituloPanelLayout);
        tituloPanelLayout.setHorizontalGroup(
            tituloPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tituloPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(cambiarDisponibilidadTituloLabel)
                .addContainerGap(374, Short.MAX_VALUE))
        );
        tituloPanelLayout.setVerticalGroup(
            tituloPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tituloPanelLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(cambiarDisponibilidadTituloLabel)
                .addGap(24, 24, 24))
        );

        getContentPane().add(tituloPanel, java.awt.BorderLayout.PAGE_START);

        infoPanel.setPreferredSize(new java.awt.Dimension(779, 110));

        fechaLabel.setFont(new java.awt.Font("Sylfaen", 0, 16)); // NOI18N
        fechaLabel.setText("Fecha: " + LocalDate.now().toString());

        horaLabel.setFont(new java.awt.Font("Sylfaen", 0, 16)); // NOI18N
        horaLabel.setText("Hora: ");

        lugarLabel.setFont(new java.awt.Font("Sylfaen", 0, 16)); // NOI18N
        lugarLabel.setText("Direccion: ");

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(horaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fechaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lugarLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(842, Short.MAX_VALUE))
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(fechaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(horaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lugarLabel)
                .addGap(98, 98, 98))
        );

        getContentPane().add(infoPanel, java.awt.BorderLayout.CENTER);

        pack();
    }                 

    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(() -> new CambiarDisponibilidadVista().setVisible(true));
    }
    
    private javax.swing.JButton cambiarDispButton;
    private javax.swing.JLabel cambiarDisponibilidadTituloLabel;
    private javax.swing.JPanel cambioDispPanel;
    private javax.swing.JButton cancelarButton;
    private javax.swing.JButton cancelarBaseButton;
    private javax.swing.JButton entrarBaseButton;
    private javax.swing.JPanel entrarBasePanel;
    private javax.swing.JComboBox<String> estadoDisponibilidadComboBox;
    private javax.swing.JLabel estadoTecnicoLabel;
    private javax.swing.JLabel fechaLabel;
    private javax.swing.JPanel funcionalidadPanel;
    private javax.swing.JLabel horaLabel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel lugarLabel;
    private javax.swing.JLabel mensajeErrorBaseLabel;
    private javax.swing.JLabel mensajeErrorLabel;
    private javax.swing.JPanel tituloPanel;                  
}