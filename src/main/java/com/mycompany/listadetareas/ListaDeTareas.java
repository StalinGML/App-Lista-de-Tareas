package com.mycompany.listadetareas;

// Programa de App - Lista de tareas en Java swing
// Desarrollado por: Stalin Mendieta

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Clase principal de la aplicación
public class ListaDeTareas extends JFrame {

    // Modelo para manejar los datos de la lista
    private DefaultListModel<String> modeloLista;
    private JList<String> listaTareas;
    private JTextField campoTarea;
    private JButton btnAgregar, btnCompletar, btnEliminar;

    // Constructor
    public ListaDeTareas() {
        // Configuración de la ventana
        setTitle("Lista de Tareas");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // Inicialización de componentes
        modeloLista = new DefaultListModel<>();
        listaTareas = new JList<>(modeloLista);

        // Personalizamos el renderizado para mostrar tareas completadas tachadas
        listaTareas.setCellRenderer(new ListCellRendererPersonalizado());

        campoTarea = new JTextField();
        btnAgregar = new JButton("Añadir Tarea");
        btnCompletar = new JButton("Marcar como Completada");
        btnEliminar = new JButton("Eliminar Tarea");

        // Layout principal
        setLayout(new BorderLayout());

        // Panel superior: campo de texto y botón agregar
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(campoTarea, BorderLayout.CENTER);
        panelSuperior.add(btnAgregar, BorderLayout.EAST);

        // Panel inferior: botones completar y eliminar
        JPanel panelInferior = new JPanel(new FlowLayout());
        panelInferior.add(btnCompletar);
        panelInferior.add(btnEliminar);

        // Scroll para la lista
        JScrollPane scrollLista = new JScrollPane(listaTareas);

        // Añadimos componentes al frame
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollLista, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // ------- MANEJADORES DE EVENTOS -------

        // 1. Agregar tarea con botón
        btnAgregar.addActionListener(e -> agregarTarea());

        // 2. Agregar tarea presionando Enter en el campo de texto
        campoTarea.addActionListener(e -> agregarTarea());

        // 3. Marcar tarea como completada
        btnCompletar.addActionListener(e -> marcarCompletada());

        // 4. Eliminar tarea seleccionada
        btnEliminar.addActionListener(e -> eliminarTarea());

        // 5. Doble clic en una tarea para marcarla como completada
        listaTareas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    marcarCompletada();
                }
            }
        });
    }

    // Método para agregar una nueva tarea
    private void agregarTarea() {
        String tarea = campoTarea.getText().trim();
        if (!tarea.isEmpty()) {
            modeloLista.addElement(tarea); // Añade tarea normal
            campoTarea.setText("");        // Limpia el campo
        } else {
            JOptionPane.showMessageDialog(this, "Escriba una tarea antes de añadir.");
        }
    }

    // Método para marcar como completada la tarea seleccionada
    private void marcarCompletada() {
        int index = listaTareas.getSelectedIndex();
        if (index != -1) {
            String tarea = modeloLista.get(index);
            // Si ya está marcada, la desmarcamos
            if (tarea.startsWith("[✓] ")) {
                modeloLista.set(index, tarea.substring(4).replace("  (Completada)", ""));
            } else {
                modeloLista.set(index, "[✓] " + tarea + "  (Completada)");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una tarea para marcarla como completada.");
        }
    }

    // Método para eliminar la tarea seleccionada
    private void eliminarTarea() {
        int index = listaTareas.getSelectedIndex();
        if (index != -1) {
            modeloLista.remove(index);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una tarea para eliminar.");
        }
    }

    // Clase interna para renderizar las tareas (tachar las completadas)
    private static class ListCellRendererPersonalizado extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            String tarea = value.toString();
            if (tarea.startsWith("[✓] ")) {
                // Cambiamos el estilo de la tarea completada
                label.setFont(label.getFont().deriveFont(Font.ITALIC));
                label.setForeground(Color.GRAY);
            } else {
                label.setFont(label.getFont().deriveFont(Font.PLAIN));
                label.setForeground(Color.BLACK);
            }
            return label;
        }
    }

    // Método main para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ListaDeTareas().setVisible(true);
        });
    }
}