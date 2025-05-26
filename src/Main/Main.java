/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

/**
 *
 * @author jazer
 */
import core.views.AirportFrame;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarkLaf;

public class Main {
    public static void main(String[] args) {
        try {
            // Aplicar tema oscuro FlatLaf
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el tema FlatDarkLaf");
            e.printStackTrace();
        }

        // Ejecutar AirportFrame en el hilo de interfaz gráfica
        java.awt.EventQueue.invokeLater(() -> {
            new AirportFrame().setVisible(true);
        });
    }
}
