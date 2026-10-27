/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package es.inf.uva.dis.interfaz.vistaycontrol.atenderllamada;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author emily
 */
public class AtenderLlamadaVista extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AtenderLlamadaVista.class.getName());
    private AtenderLlamadaControlador controlador;

    public AtenderLlamadaVista() {
        controlador = new AtenderLlamadaControlador(this);
        initComponents();
        refrescarVentana();
    }

    private void initComponents() {
        fuenteSubtitulos = new Font("Segoe UI", Font.BOLD, 14);
        fuenteTitulosPanel = new Font("Segoe UI", Font.BOLD, 17);
        fuenteBotonesYCampos = new Font("Segoe UI", Font.PLAIN, 12);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Atender Llamada");
        
        getContentPane().setLayout(new BorderLayout());

        // --- PANEL SUR: ERRORES ---
        panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        mensajeLabel = new JLabel(" "); 
        mensajeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelEstado.add(mensajeLabel);

        // --- CONTENEDOR CENTRAL: PANELES DE DATOS ---
        JPanel contenedorPaneles = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));

        // --- DISEÑO PANEL 1 ---
        datosLlamadaPanel = new JPanel(new AbsoluteLayout());
        datosLlamadaPanel.setBorder(BorderFactory.createTitledBorder(null, "Datos de la Llamada", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, fuenteTitulosPanel));
        datosLlamadaPanel.setPreferredSize(new Dimension(300, 460));

        numTelefonoLlamadaTextField = new JTextField(12);
        numTelefonoLlamadaTextField.setFont(fuenteBotonesYCampos);
        numContratoTextField = new JTextField(12);
        numContratoTextField.setFont(fuenteBotonesYCampos);
        nifCifTextField = new JTextField(12);
        nifCifTextField.setFont(fuenteBotonesYCampos);
        tlfContactoTextField = new JTextField(12);
        tlfContactoTextField.setFont(fuenteBotonesYCampos);
        
        cancelarLlamadaButton = new JButton("Cancelar");
        cancelarLlamadaButton.setFont(fuenteBotonesYCampos);
        validarDatosAportadosButton = new JButton("Validar");
        validarDatosAportadosButton.setFont(fuenteBotonesYCampos);
        noAportaDatosLlamadaButton = new JButton("No aporta datos");
        noAportaDatosLlamadaButton.setFont(fuenteBotonesYCampos);
        
        // BOTÓN: Ir a Menú (Oculto inicialmente)
        irAMenuButton = new JButton("Ir a Menú");
        irAMenuButton.setFont(fuenteBotonesYCampos);
        irAMenuButton.setToolTipText("Regresar al menú principal del Coordinador");
        irAMenuButton.setVisible(false);

        tlfOrigenLabel = new JLabel("Teléfono origen:");
        tlfOrigenLabel.setFont(fuenteSubtitulos);
        datosLlamadaPanel.add(tlfOrigenLabel, new AbsoluteConstraints(15, 35, -1, -1));
        datosLlamadaPanel.add(numTelefonoLlamadaTextField, new AbsoluteConstraints(15, 60, 240, 30));

        contratoLabel = new JLabel("Nº Contrato:");
        contratoLabel.setFont(fuenteSubtitulos);
        datosLlamadaPanel.add(contratoLabel, new AbsoluteConstraints(15, 105, -1, -1));
        datosLlamadaPanel.add(numContratoTextField, new AbsoluteConstraints(15, 130, 240, 30));

        JLabel lblNif = new JLabel("NIF/CIF:");
        lblNif.setFont(fuenteSubtitulos);
        datosLlamadaPanel.add(lblNif, new AbsoluteConstraints(15, 175, -1, -1));
        datosLlamadaPanel.add(nifCifTextField, new AbsoluteConstraints(15, 200, 240, 30));

        JLabel lblTlfContacto = new JLabel("Teléfono contacto:");
        lblTlfContacto.setFont(fuenteSubtitulos);
        datosLlamadaPanel.add(lblTlfContacto, new AbsoluteConstraints(15, 245, -1, -1));
        datosLlamadaPanel.add(tlfContactoTextField, new AbsoluteConstraints(15, 270, 240, 30));

        datosLlamadaPanel.add(noAportaDatosLlamadaButton, new AbsoluteConstraints(15, 320, 160, 35));
        datosLlamadaPanel.add(cancelarLlamadaButton, new AbsoluteConstraints(15, 410, 100, 35));
        datosLlamadaPanel.add(validarDatosAportadosButton, new AbsoluteConstraints(165, 410, 100, 35));
        
        // Posicionamos el botón encima del espacio de los de control de flujo iniciales
        datosLlamadaPanel.add(irAMenuButton, new AbsoluteConstraints(15, 410, 250, 35));

        // --- DISEÑO PANEL 3 ---
        confirmacionDatosPanel = new JPanel(new AbsoluteLayout());
        confirmacionDatosPanel.setBorder(BorderFactory.createTitledBorder(null, "Confirmación", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, fuenteTitulosPanel));
        confirmacionDatosPanel.setPreferredSize(new Dimension(450, 460));
        confirmacionDatosPanel.setVisible(false);

        // Nombre
        JLabel lblTituloNombre = new JLabel("Nombre/Razón Social:");
        lblTituloNombre.setFont(fuenteSubtitulos);
        confirmacionDatosPanel.add(lblTituloNombre, new AbsoluteConstraints(15, 30, -1, -1));

        nombreDinamicoLabel = new JLabel("---"); // Se actualizará desde el controlador
        nombreDinamicoLabel.setFont(fuenteBotonesYCampos);
        confirmacionDatosPanel.add(nombreDinamicoLabel, new AbsoluteConstraints(20, 55, 410, 20));

        coincideNombreRadioButton = new JRadioButton("Coincide");
        coincideNombreRadioButton.setFont(fuenteBotonesYCampos);
        noCoincideNombreRadioButton = new JRadioButton("No coincide");
        noCoincideNombreRadioButton.setFont(fuenteBotonesYCampos);
        nombreButtonGroup = new ButtonGroup();
        nombreButtonGroup.add(coincideNombreRadioButton);
        nombreButtonGroup.add(noCoincideNombreRadioButton);
        confirmacionDatosPanel.add(coincideNombreRadioButton, new AbsoluteConstraints(15, 85, -1, -1));
        confirmacionDatosPanel.add(noCoincideNombreRadioButton, new AbsoluteConstraints(140, 85, -1, -1));

        // Teléfono
        JLabel lblTituloTlf = new JLabel("Nº Teléfono Contacto:");
        confirmacionDatosPanel.add(lblTituloTlf, new AbsoluteConstraints(15, 130, -1, -1));

        tlfDinamicoLabel = new JLabel("---"); // Se actualizará desde el controlador
        tlfDinamicoLabel.setFont(fuenteBotonesYCampos);
        tlfDinamicoLabel.setForeground(new Color(0, 51, 153));
        confirmacionDatosPanel.add(tlfDinamicoLabel, new AbsoluteConstraints(20, 155, 410, 20));

        coincideTlfRadioButton = new JRadioButton("Coincide");
        coincideTlfRadioButton.setFont(fuenteBotonesYCampos);
        noCoincideTlfRadioButton = new JRadioButton("No coincide");
        noCoincideTlfRadioButton.setFont(fuenteBotonesYCampos);
        tlfButtonGroup = new ButtonGroup();
        tlfButtonGroup.add(coincideTlfRadioButton);
        tlfButtonGroup.add(noCoincideTlfRadioButton);
        confirmacionDatosPanel.add(coincideTlfRadioButton, new AbsoluteConstraints(15, 185, -1, -1));
        confirmacionDatosPanel.add(noCoincideTlfRadioButton, new AbsoluteConstraints(140, 185, -1, -1));

        // Ubicación
        JLabel lblUbi = new JLabel("Ubicación:");
        lblUbi.setFont(fuenteSubtitulos);
        confirmacionDatosPanel.add(lblUbi, new AbsoluteConstraints(15, 225, -1, -1));

        ubicacionesComboBox = new JComboBox<>(new String[] { "-- Seleccione una ubicación --", "Ubicación 1", "No hay ubicación coincidente" });
        ubicacionesComboBox.setFont(fuenteBotonesYCampos);
        confirmacionDatosPanel.add(ubicacionesComboBox, new AbsoluteConstraints(15, 255, 410, 35));

        noAportaDatosButton = new JButton("No aporta datos diferentes");
        noAportaDatosButton.setFont(fuenteBotonesYCampos);
        confirmacionDatosPanel.add(noAportaDatosButton, new AbsoluteConstraints(15, 410, 210, 35));

        // BOTÓN VALIDAR
        validarConfirmacionButton = new JButton("Validar");
        validarConfirmacionButton.setFont(fuenteBotonesYCampos);
        confirmacionDatosPanel.add(validarConfirmacionButton, new AbsoluteConstraints(330, 410, 100, 35));

         // --- DISEÑO PANEL 4 ---
        motivoPanel = new JPanel(new AbsoluteLayout());
        motivoPanel.setBorder(BorderFactory.createTitledBorder(null, "¿Motivo?", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, fuenteTitulosPanel));
        motivoPanel.setPreferredSize(new Dimension(250, 460));
        motivoPanel.setVisible(false);

        consultaButton = new JButton("Consulta");
        incidenciaButton = new JButton("Incidencia");
        motivoPanel.add(consultaButton, new AbsoluteConstraints(25, 80, 200, 70));
        motivoPanel.add(incidenciaButton, new AbsoluteConstraints(25, 200, 200, 70));

        
        contenedorPaneles.add(datosLlamadaPanel);
        contenedorPaneles.add(confirmacionDatosPanel);
        contenedorPaneles.add(motivoPanel);

        this.getContentPane().add(contenedorPaneles, BorderLayout.CENTER);
        this.getContentPane().add(panelEstado, BorderLayout.SOUTH);

        // --- EVENTOS ---
        // 1er Panel
        cancelarLlamadaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {                
                cancelarLlamadaButtonActionPerformed(evt);
            }
        });

        validarDatosAportadosButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    validarDatosAportadosButtonActionPerformed(evt);
            }
        });

        noAportaDatosLlamadaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noAportaDatosLlamadaButtonActionPerformed(evt);
            }
        });
        
        // Manejador del botón de seguridad para fallos relacionales críticos
        irAMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irAMenuButtonActionPerformed(evt);
            }
        });
        
        // 3er Panel
        noAportaDatosButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noAportaDatosButtonActionPerformed(evt);
            }
        });

        validarConfirmacionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    validarConfirmacionButtonActionPerformed(evt);
            }
        });

        // 4to Panel
        consultaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultaButtonActionPerformed(evt);
            }
        });

        incidenciaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incidenciaButtonActionPerformed(evt);
            }
        });

    }
    
    private boolean estaStringVacio(String cadena) {
        return cadena == null || cadena.trim().isEmpty();
    }
    
    /**
     * Comprueba si el formato del teléfono de origen es válido (9 dígitos numéricos).
     * Mapea las necesidades de robustez de PA-32, PA-33 y PA-34.
     * @param telefono cadena a validar
     * @return true si cumple el formato, false en caso contrario
     */
    private boolean esNumeroTelefonoOrigenValido(String telefono) {
        if (telefono == null) {
            return false;
        }
        return telefono.trim().matches("^\\d{9}$");
    }
    
    
    // --- MÉTODOS DE LANZAMIENTO DE EVENTOS HACIA EL CONTROLADOR ---
    private void cancelarLlamadaButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controlador.procesaEventoCancelarEnInicioLlamada();
    }
    
    private void irAMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controlador.procesaEventoCancelarEnInicioLlamada();
    }

    //Primera validación
    private void validarDatosAportadosButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String tlfOrigen = getNumTelefonoLlamadaOrigen();
        
        if (estaStringVacio(tlfOrigen) ||
            estaStringVacio(geNumContratoTextField()) || 
            estaStringVacio(getNifCifTextField()) ||
            estaStringVacio(getTlfContactoTextField())) {
            mostrarMensajeError("ERROR: Hay campos vacíos");
        } else if (!esNumeroTelefonoOrigenValido(tlfOrigen)) { // Uso de la nueva función de validación dedicada
            mostrarMensajeError("ERROR: El número de teléfono erróneo.");
        } else {                    
            mostrarMensajeError("");
            controlador.procesaEventoValidarDatosAportados();
        } 
    }

    private void noAportaDatosLlamadaButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String tlfOrigen = getNumTelefonoLlamadaOrigen();
        if (esNumeroTelefonoOrigenValido(tlfOrigen))
            controlador.procesaEventoNoApartoDatos();
        else 
           mostrarMensajeError("ERROR: El número de teléfono erróneo.");
    }

    private void noAportaDatosButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controlador.procesaEventoNoAportaDatosDiferentes();
    }

    //Segunda validación
    private void validarConfirmacionButtonActionPerformed(java.awt.event.ActionEvent evt) {
        boolean nombreSeleccionado = nombreButtonGroup.getSelection() != null;
        boolean tlfSeleccionado = tlfButtonGroup.getSelection() != null;
        boolean ubicacionValida = ubicacionesComboBox.getSelectedItem() != null && !ubicacionesComboBox.getSelectedItem().toString().equals("-- Seleccione una ubicación --");

        if (!nombreSeleccionado || !tlfSeleccionado || !ubicacionValida) {
            mostrarMensajeError("ERROR: Debe confirmar nombre, teléfono y seleccionar una ubicación.");
        } else {
            mostrarMensajeError(""); 
            controlador.procesaEventoValidarConfirmacion();
        }
    }


    private void consultaButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controlador.procesaEventoConsulta();
    }

    private void incidenciaButtonActionPerformed(java.awt.event.ActionEvent evt) {
        controlador.procesaEventoIncidencia();
    }
    
    // --- MÉTODOS DE COMUNICACIÓN PARA EL CONTROLADOR ---
    
    public String getNumTelefonoLlamadaOrigen() {
        return numTelefonoLlamadaTextField.getText();
    }
    
    public String geNumContratoTextField() {
        return numContratoTextField.getText();
    }
    
    public String getNifCifTextField() {
        return nifCifTextField.getText();
    }

    public String getTlfContactoTextField() {
        return tlfContactoTextField.getText();
    }
    
    public void setNombreClienteConfirmacion(String nombre) {
        nombreDinamicoLabel.setText(nombre);
    }

    public void setTlfClienteConfirmacion(String telefono) {
        tlfDinamicoLabel.setText(telefono);
    }
    
    public boolean coincideNombreRazonSocial() {
        return coincideNombreRadioButton.isSelected();
    }
    
    public boolean coincideTlfRadioButton() {
        return coincideTlfRadioButton.isSelected();
    }

    public boolean coincideUbicacion() {
        return !ubicacionesComboBox.getSelectedItem().toString().equals("No hay ubicación coincidente");
    }
    
    public String getUbicacionSeleccionada() {
        return ubicacionesComboBox.getSelectedItem().toString();
    }

    public void mostrarMensajeError(String mensaje) {
        mensajeLabel.setText(mensaje.isEmpty() ? " " : mensaje);
        mensajeLabel.setForeground(Color.RED);
    }
    
    public void mostrarMensajeDeLlamadaAlmacenada(String mensaje) {
        mensajeLabel.setText(mensaje.isEmpty() ? " " : mensaje);
        mensajeLabel.setForeground(Color.BLUE);
        mostrarBotonIrAMenu();   
    }
    
    public void mostrarMensajeNotImplementedYet() {
        mensajeLabel.setText("NotImplementedYet");
        mensajeLabel.setForeground(Color.GRAY);
        mostrarBotonIrAMenu();   
    }

    public void mostrarPanelConfirmacion() {
        numTelefonoLlamadaTextField.setEditable(false);
        numContratoTextField.setEditable(false);
        nifCifTextField.setEditable(false);
        tlfContactoTextField.setEditable(false);
        confirmacionDatosPanel.setVisible(true);
        cancelarLlamadaButton.setVisible(false);
        validarDatosAportadosButton.setVisible(false);
        noAportaDatosLlamadaButton.setVisible(false);
        irAMenuButton.setVisible(false);
        
        refrescarVentana();
    }

    public void mostrarPanelMotivo() {
        coincideNombreRadioButton.setEnabled(false);
        noCoincideNombreRadioButton.setEnabled(false);
        coincideTlfRadioButton.setEnabled(false);
        noCoincideTlfRadioButton.setEnabled(false);
        ubicacionesComboBox.setEnabled(false);
        motivoPanel.setVisible(true);
        noAportaDatosButton.setVisible(false);
        validarConfirmacionButton.setVisible(false);

        refrescarVentana();
    }
    
    public void premarcarCoincidenciaTelefono(boolean coincide) {
        if (coincide) {
            coincideTlfRadioButton.setSelected(true);
        } else {
            noCoincideTlfRadioButton.setSelected(true);
        }
    }

    public void cargarUbicacionesEnCombo(java.util.List<String> listaUbicaciones) {
        ubicacionesComboBox.removeAllItems();
        ubicacionesComboBox.addItem("-- Seleccione una ubicación --");
        for (String ubi : listaUbicaciones) {
            ubicacionesComboBox.addItem(ubi);
        }
        ubicacionesComboBox.addItem("No hay ubicación coincidente");
    }

    /**
     * Regresa la pantalla al estado inicial editable permitiendo corregir campos tras fallos alternativos,
     * o bloquea la edición mostrando exclusivamente el botón "Ir a Menú".
     */
    public void regresarAPanelEdicion(boolean esErrorCritico) {
        if (esErrorCritico) {
            numTelefonoLlamadaTextField.setEditable(false);
            numContratoTextField.setEditable(false);
            nifCifTextField.setEditable(false);
            tlfContactoTextField.setEditable(false);
            
            confirmacionDatosPanel.setVisible(false);
            validarDatosAportadosButton.setVisible(false);
            noAportaDatosLlamadaButton.setVisible(false);
            cancelarLlamadaButton.setVisible(false);
            
            irAMenuButton.setVisible(true);
        } else {
            numTelefonoLlamadaTextField.setEditable(true);
            numContratoTextField.setEditable(true);
            nifCifTextField.setEditable(true);
            tlfContactoTextField.setEditable(true);
            
            confirmacionDatosPanel.setVisible(false);
            cancelarLlamadaButton.setVisible(true);
            validarDatosAportadosButton.setVisible(true);
            noAportaDatosLlamadaButton.setVisible(true);
            irAMenuButton.setVisible(false);
        }
        
        refrescarVentana();
    }
    
    private void mostrarBotonIrAMenu() {
        numTelefonoLlamadaTextField.setEditable(false);
        numContratoTextField.setEditable(false);
        nifCifTextField.setEditable(false);
        tlfContactoTextField.setEditable(false);

        cancelarLlamadaButton.setVisible(false);
        noAportaDatosLlamadaButton.setVisible(false);
        noAportaDatosButton.setVisible(false);
        validarDatosAportadosButton.setVisible(false);
        validarConfirmacionButton.setVisible(false);
        consultaButton.setVisible(false);
        incidenciaButton.setVisible(false);
        irAMenuButton.setVisible(true);
        
        coincideNombreRadioButton.setEnabled(false);
        noCoincideNombreRadioButton.setEnabled(false);
        coincideTlfRadioButton.setEnabled(false);
        noCoincideTlfRadioButton.setEnabled(false);
    
        ubicacionesComboBox.setEditable(false);
    }
    
    private void refrescarVentana() {
        pack();
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(() -> new AtenderLlamadaVista().setVisible(true));
    }
    
    private JPanel panelEstado;
    private JPanel datosLlamadaPanel;
    private JPanel confirmacionDatosPanel;
    private JPanel motivoPanel;
    
    private JLabel tlfOrigenLabel;
    private JLabel contratoLabel;
    private JLabel mensajeLabel;
    private JLabel nombreDinamicoLabel;
    private JLabel tlfDinamicoLabel;

    private JTextField numTelefonoLlamadaTextField;
    private JTextField numContratoTextField;
    private JTextField nifCifTextField;
    private JTextField tlfContactoTextField;

    private JButton cancelarLlamadaButton;
    private JButton noAportaDatosLlamadaButton;
    private JButton noAportaDatosButton;
    private JButton validarDatosAportadosButton;
    private JButton validarConfirmacionButton;
    private JButton consultaButton;
    private JButton incidenciaButton;
    private JButton irAMenuButton;

    private JRadioButton coincideNombreRadioButton;
    private JRadioButton noCoincideNombreRadioButton;
    private JRadioButton coincideTlfRadioButton;
    private JRadioButton noCoincideTlfRadioButton;
    
    private JComboBox<String> ubicacionesComboBox;

    private ButtonGroup nombreButtonGroup;
    private ButtonGroup tlfButtonGroup;
    
    private Font fuenteSubtitulos;
    private Font fuenteTitulosPanel;
    private Font fuenteBotonesYCampos;

    
}