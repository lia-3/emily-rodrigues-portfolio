package es.inf.uva.dis.interfaz.vistaycontrol.consultarincidencias;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import es.inf.uva.dis.negocio.modelos.Incidencia;

/**
 * Vista oficial para la consulta de incidencias por parte del Supervisor.
 * e integra la sección de detalles del boceto interactivo.
 * * @author emily
 * @author diego
 */
public class ConsultarIncidenciasVista extends javax.swing.JFrame {

    private ConsultarIncidenciasControlador controladorIncidencias = new ConsultarIncidenciasControlador(this);

    public ConsultarIncidenciasVista() {
        initComponents();
        configurarCalendario();

        setTitle("Consultar Incidencias - Supervisor");
        java.awt.Dimension dimensiones = new java.awt.Dimension(1250, 600); // Ajustado levemente para acomodar los detalles
        setSize(dimensiones);
        setMinimumSize(dimensiones);
        setLocationRelativeTo(null);
        listaIncidenciasVisiblesList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaIncidenciasVisiblesList1.setModel(new DefaultListModel<>());
        masDetallesButton.setEnabled(false);
        errorSinIncidenciasLabel.setVisible(false);
    }

    /**
     * Configura el comportamiento del calendario (JCalendar).
     * Incluye la validación de fechas futuras y la detección de cambios de día.
     */
    private void configurarCalendario() {
        calendarioCalendar.setMaxSelectableDate(new java.util.Date());

        calendarioCalendar.getDayChooser().addDateEvaluator(new com.toedter.calendar.IDateEvaluator() {
            private java.util.Date truncarHora(java.util.Date fecha) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(fecha);
                cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
                cal.set(java.util.Calendar.MINUTE, 0);
                cal.set(java.util.Calendar.SECOND, 0);
                cal.set(java.util.Calendar.MILLISECOND, 0);
                return cal.getTime();
            }
            @Override public boolean isInvalid(java.util.Date date) {
                return truncarHora(date).after(truncarHora(new java.util.Date()));
            }
            @Override public java.awt.Color getInvalidForegroundColor() { return java.awt.Color.LIGHT_GRAY; }
            @Override public java.awt.Color getInvalidBackroundColor() { return null; }
            @Override public String getInvalidTooltip() { return "Fecha no disponible"; }
            @Override public boolean isSpecial(java.util.Date date) { return false; }
            @Override public java.awt.Color getSpecialForegroundColor() { return null; }
            @Override public java.awt.Color getSpecialBackroundColor() { return null; }
            @Override public String getSpecialTooltip() { return null; }
        });

        java.awt.Component[] components = calendarioCalendar.getDayChooser().getDayPanel().getComponents();
        for (java.awt.Component comp : components) {
            comp.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (comp.isEnabled()) {
                        listaIncidenciasVisiblesList1.setModel(new DefaultListModel<>());
                        detallesTextArea.setText(""); // Limpiar detalles al cambiar de fecha
                        errorSinIncidenciasLabel.setVisible(false);
                        controladorIncidencias.procesaEventoSeleccionFecha();
                    }
                }
            });
        }
    }

    /**
     * Muestra la lista de incidencias en el JList.
     * @param listaIncidenciaList Lista de objetos Incidencia obtenidos por el controlador.
     */
    public void mostrarIncidencias(List<Incidencia> listaIncidenciaList) {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Incidencia incidencia : listaIncidenciaList) {
            modelo.addElement(incidencia.toString());
        }
        masDetallesButton.setEnabled(false);
        listaIncidenciasVisiblesList1.setModel(modelo);
        listaIncidenciasVisiblesList1.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) masDetallesButton.setEnabled(true);
        });
        errorSinIncidenciasLabel.setVisible(false);
    }

    /**
     * Muestra un mensaje de error en el panel izquierdo (bajo el calendario).
     * @param mensaje El mensaje a mostrar (ej. "No hay incidencias").
     */
    public void mostrarMensajeError(String mensaje) {
        listaIncidenciasVisiblesList1.setModel(new DefaultListModel<>());
        detallesTextArea.setText(""); 
        masDetallesButton.setEnabled(false);
        errorSinIncidenciasLabel.setText(mensaje);
        errorSinIncidenciasLabel.setVisible(mensaje != null && !mensaje.isEmpty());
    }

    /**
     * Permite actualizar el área de texto con la información detallada de la incidencia.
     * @param textoDetalles Texto descriptivo detallado.
     */
    public void mostrarDetallesIncidencia(String textoDetalles) {
        detallesTextArea.setText(textoDetalles);
    }
    
     public void limpiarDetalles(){
        detallesTextArea.setText("");
     }

    /**
     * Devuelve la fecha seleccionada por el usuario en el calendario.
     * @return Objeto Date con la fecha.
     */
    public Date getFechaSeleccionada() {
        return calendarioCalendar.getDate();
    }

    public String getValorFiltro() {
        return (String) filtroComboBox.getSelectedItem();
    }

    // --- MANEJADORES DE EVENTOS ---

    private void cancelarButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        controladorIncidencias.procesaEventoCancelar();
    }                                              

    private void masDetallesButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controladorIncidencias.procesaEventoMasDetalles();
    }
    private void filtroComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        controladorIncidencias.procesaEventoFiltrar();
    }   
    

    /**
     * Inicialización y maquetación limpia de los componentes.
     */
    private void initComponents() {
        fechaPanel = new javax.swing.JPanel();
        seleccionaFechaLabel = new javax.swing.JLabel();
        calendarioCalendar = new com.toedter.calendar.JCalendar();
        errorSinIncidenciasLabel = new javax.swing.JLabel();
        
        incidenciasPanel = new javax.swing.JPanel();
        incidenciasLabel = new javax.swing.JLabel();
        filtroComboBox = new javax.swing.JComboBox<>();
        incidenciasScrollPane = new javax.swing.JScrollPane();
        listaIncidenciasVisiblesList1 = new javax.swing.JList<>();
        
        // Nuevos componentes del boceto (con nombres significativos)
        detallesLabel = new javax.swing.JLabel();
        detallesScrollPane = new javax.swing.JScrollPane();
        detallesTextArea = new javax.swing.JTextArea();
        
        botoneraPanel = new javax.swing.JPanel();
        cancelarButton = new javax.swing.JButton();
        masDetallesButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        javax.swing.JPanel centroPanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 2));
        
        // --- PANEL IZQUIERDO: SELECCIÓN DE FECHA ---
        fechaPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        seleccionaFechaLabel.setFont(new Font("Segoe UI", 0, 18));
        seleccionaFechaLabel.setText("Selecciona una fecha");
        fechaPanel.add(seleccionaFechaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));
        fechaPanel.add(calendarioCalendar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 330, 180));
        
        errorSinIncidenciasLabel.setFont(new Font("Segoe UI", 1, 12));
        errorSinIncidenciasLabel.setForeground(new Color(255, 51, 51));
        fechaPanel.add(errorSinIncidenciasLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 330, 30));
        centroPanel.add(fechaPanel);

        // --- PANEL DERECHO: LISTADO Y DETALLES DE INCIDENCIAS ---
        incidenciasPanel.setBorder(new EmptyBorder(20, 20, 0, 40));
        incidenciasPanel.setLayout(new java.awt.BorderLayout(0, 15));
        
        // Cabecera del panel derecho (Título + Filtro)
        javax.swing.JPanel cabeceraDerecha = new javax.swing.JPanel(new java.awt.BorderLayout());
        incidenciasLabel.setFont(new Font("Segoe UI", 0, 18));
        incidenciasLabel.setText("Incidencias");
        cabeceraDerecha.add(incidenciasLabel, java.awt.BorderLayout.WEST);
        
        filtroComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Resuelta Remotamente", "Resuelta In Situ", "No Resuelta" }));
        filtroComboBox.addActionListener(evt -> controladorIncidencias.procesaEventoFiltrar());
        cabeceraDerecha.add(filtroComboBox, java.awt.BorderLayout.EAST);
        incidenciasPanel.add(cabeceraDerecha, java.awt.BorderLayout.NORTH);
        
        // Cuerpo interno derecho usando GridBagLayout para organizar la lista y la sección de detalles
        javax.swing.JPanel cuerpoIncidenciasPanel = new javax.swing.JPanel(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        
        // JList de Incidencias
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.55; // Proporción de tamaño para la lista
        gbc.insets = new java.awt.Insets(0, 0, 10, 0);
        incidenciasScrollPane.setViewportView(listaIncidenciasVisiblesList1);
        cuerpoIncidenciasPanel.add(incidenciasScrollPane, gbc);
        listaIncidenciasVisiblesList1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Detecta doble clic
                    int index = listaIncidenciasVisiblesList1.locationToIndex(e.getPoint()); // Obtiene el índice donde se hizo clic
                    if (index >= 0) {
                        String elemento = listaIncidenciasVisiblesList1.getModel().getElementAt(index);
                   
                    }
                }
                limpiarDetalles();
            }
        });
        
        // Etiqueta de Detalles (procedente de jLabel4 del boceto)
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        gbc.insets = new java.awt.Insets(5, 0, 5, 0);
        detallesLabel.setFont(new Font("Segoe UI", 0, 18));
        detallesLabel.setText("Detalles");
        cuerpoIncidenciasPanel.add(detallesLabel, gbc);
        
        // Área de Texto para Detalles (procedente de jScrollPane2 del boceto)
        gbc.gridy = 2;
        gbc.weighty = 0.45; // Proporción de tamaño para los detalles descriptivos
        gbc.insets = new java.awt.Insets(0, 0, 0, 0);
        detallesTextArea.setEditable(false); // Por defecto solo lectura hasta que se seleccione algo
        detallesTextArea.setColumns(20);
        detallesTextArea.setLineWrap(true);
        detallesTextArea.setWrapStyleWord(true);
        detallesTextArea.setFont(new Font("Segoe UI", 0, 13));
        detallesScrollPane.setViewportView(detallesTextArea);
        cuerpoIncidenciasPanel.add(detallesScrollPane, gbc);
        
        incidenciasPanel.add(cuerpoIncidenciasPanel, java.awt.BorderLayout.CENTER);
        centroPanel.add(incidenciasPanel);
        
        this.getContentPane().add(centroPanel, java.awt.BorderLayout.CENTER);

        // --- PANEL INFERIOR: BOTONERA ---
        botoneraPanel.setLayout(new java.awt.BorderLayout());
        botoneraPanel.setBorder(new EmptyBorder(10, 40, 20, 40));
        
        cancelarButton.setText("Cancelar");
        cancelarButton.addActionListener(evt -> controladorIncidencias.procesaEventoCancelar());
        botoneraPanel.add(cancelarButton, java.awt.BorderLayout.WEST);

        masDetallesButton.setText("Más Detalles");
        masDetallesButton.addActionListener(evt -> controladorIncidencias.procesaEventoMasDetalles());
        botoneraPanel.add(masDetallesButton, java.awt.BorderLayout.EAST);

        this.getContentPane().add(botoneraPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ConsultarIncidenciasVista().setVisible(true));
    }

    // Declaración de Variables con nombres significativos
    private javax.swing.JPanel fechaPanel;
    private javax.swing.JPanel incidenciasPanel;
    private javax.swing.JPanel botoneraPanel;
    private javax.swing.JButton cancelarButton;
    private javax.swing.JButton masDetallesButton;
    private com.toedter.calendar.JCalendar calendarioCalendar;
    private javax.swing.JComboBox<String> filtroComboBox;
    private javax.swing.JLabel seleccionaFechaLabel;
    private javax.swing.JLabel errorSinIncidenciasLabel;
    private javax.swing.JLabel incidenciasLabel;
    private javax.swing.JList<String> listaIncidenciasVisiblesList1;
    private javax.swing.JScrollPane incidenciasScrollPane;
    
    // Componentes del Boceto renombrados e integrados con éxito
    private javax.swing.JLabel detallesLabel;
    private javax.swing.JScrollPane detallesScrollPane;
    private javax.swing.JTextArea detallesTextArea;
}