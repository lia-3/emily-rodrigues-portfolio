/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package es.inf.uva.dis.interfaz.vistaycontrol.identificarse;
import es.inf.uva.dis.servicioscomunes.excepciones.UsuarioInactivoException;
import java.awt.CardLayout;
import java.awt.Color;
/**
 *
 * @author diego
 */
public class IdentificacionVista extends javax.swing.JFrame {
    
    private IdentificacionControlador controladorVista; 
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(IdentificacionVista.class.getName());
    private CardLayout loginLayout;
    
    /**
     * Creates new form EmpleadoVista
     */
    public IdentificacionVista() {
        initComponents();
        controladorVista= new IdentificacionControlador(this);
        setLocationRelativeTo(null);
        setTitle("Iniciar Sesión");
    }
    
    public String getLogin(){
        return loginTextField.getText();
    }
    
    public String getPassword(){
        return passwordTextField.getText();
    }
    
    public void limpiarCamposEscritos(){
        loginTextField.setText("");
        passwordTextField.setText("");
    }
    
    public void limpiarError(){
        errorLabel.setText("");
    }
    public void mostrarMensajeError(String mensajeError){
        errorLabel.setForeground(Color.red);
        errorLabel.setText(mensajeError);
    }
    
    public void mostrarExito(){
        errorLabel.setForeground(Color.green);
        errorLabel.setText("Exito");
    }
    

    @SuppressWarnings("unchecked")
    // ===== COMPONENTES INTERFAZ =====                          
    private void initComponents() {

        InicioSesionPanel = new javax.swing.JPanel();
        passwordTextField = new javax.swing.JTextField();
        loginTextField = new javax.swing.JTextField();
        loginLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        identificacionUsusarioLabel = new javax.swing.JLabel();
        aceptarButton = new javax.swing.JButton();
        errorLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(483, 234));
        setMinimumSize(new java.awt.Dimension(675, 400));
        getContentPane().setLayout(new java.awt.CardLayout());

        InicioSesionPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        InicioSesionPanel.add(passwordTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 200, 328, -1));

        InicioSesionPanel.add(loginTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 138, 328, -1));

        loginLabel.setText("Login:");
        InicioSesionPanel.add(loginLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 116, 89, -1));

        passwordLabel.setText("Password:");
        InicioSesionPanel.add(passwordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 178, 65, -1));

        identificacionUsusarioLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        identificacionUsusarioLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        identificacionUsusarioLabel.setText("Identificación de Usuario");
        identificacionUsusarioLabel.setName(""); // NOI18N
        InicioSesionPanel.add(identificacionUsusarioLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, -1, 28));

        aceptarButton.setText("Aceptar");
        aceptarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarButtonActionPerformed(evt);
            }
        });
        InicioSesionPanel.add(aceptarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 252, 328, -1));

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));
        errorLabel.setToolTipText("");
        InicioSesionPanel.add(errorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 320, 40));

        getContentPane().add(InicioSesionPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>                                                                             

    public boolean compruebaCharArrayNoVacio(String cadena){
        return cadena!=null && !cadena.isEmpty();
    }
    
    private void aceptarButtonActionPerformed(java.awt.event.ActionEvent evt){                                              
        if(compruebaCharArrayNoVacio(loginTextField.getText()) && 
           compruebaCharArrayNoVacio(passwordTextField.getText())){
            controladorVista.procesaEventoIdentificarse();
        }else{
            mostrarMensajeError("Error: Algun campo está incompleto");
        }
    }                                             

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new IdentificacionVista().setVisible(true));
    }

    // Variables declaration - do not modify   
    private javax.swing.JPanel InicioSesionPanel;
    private javax.swing.JButton aceptarButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel identificacionUsusarioLabel;
    private javax.swing.JLabel loginLabel;
    private javax.swing.JTextField loginTextField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField passwordTextField;
    // End of variables declaration                   
}
