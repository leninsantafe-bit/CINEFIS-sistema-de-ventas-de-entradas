/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fis.poo.cinefis.vista;

import fis.poo.cinefis.modelo.Funcion;
import fis.poo.cinefis.modelo.Pelicula;
import fis.poo.cinefis.modelo.Sala;
import fis.poo.cinefis.repositorio.RepositorioFunciones;
import fis.poo.cinefis.repositorio.RepositorioPeliculas;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;

    public class VistaRegistroPeliculas extends javax.swing.JFrame {
    private File posterSeleccionado;
    private ArrayList<Funcion> listaFunciones = new ArrayList<>();
    private String RutaImagen="";
    public VistaRegistroPeliculas() {
        initComponents();
        

        configurarTabla();
        configurarArrastrarYSoltar();
        cargarDatosComboBox();
        configurarListeners();
    }
    
    private void configurarListeners() {
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String titulo = txtNombrePelicula.getText().trim();
                String fecha = txtFecha.getText().trim();
                String hora = txtHora.getText().trim();
                String textoPrecio = txtPrecio.getText().trim();
                String genero = CbGenero.getSelectedItem().toString();
                String clasificacion = CbClasificacion.getSelectedItem().toString();
                String sala = CbSala.getSelectedItem().toString();
                int duracion=0;
                String sinopsis = txtSinopsis.getText().trim();
                
                if (titulo.isEmpty() || fecha.isEmpty() || hora.isEmpty() || textoPrecio.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos de texto.");
                    return; 
                }
                double precioFinal = 0.0;
                try {
                    precioFinal = Double.parseDouble(textoPrecio);
                } catch (NumberFormatException e) {
                    javax.swing.JOptionPane.showMessageDialog(null, "El precio es inválido. Use números (Ej: 5.50)");
                    return; // Cortamos la ejecución aquí
                }

                if (!fecha.isEmpty()) {
                    try {
                        // Le decimos cuál es el formato estricto que esperamos
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        // Intentamos convertir el texto a una Fecha real
                        LocalDate.parse(fecha, formatter);

                        // Si el código llega aquí y no saltó al 'catch', la fecha es perfecta

                    } catch (DateTimeParseException e) {
                        // Si el usuario escribió "hola" o "32/01/2026", Java lanza este error
                        JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use DD/MM/YYYY (Ej: 25/12/2026)", "Error de Fecha", JOptionPane.ERROR_MESSAGE);
                        
                        txtFecha.setText(""); // Borramos el texto malo
                        fecha = "";  // Reiniciamos la variable
                        
                        return; 
                    }
                }
            
                // 1. Primero creas los objetos (instancias) de ambos repositorios
                RepositorioFunciones repoFunciones = new RepositorioFunciones();
                RepositorioPeliculas repoPeliculas = new RepositorioPeliculas();

                // 2. Ahora llamas a los métodos usando esos objetos (en minúscula)
                String codFuncion = repoFunciones.generarSiguienteCodigoFuncion();
                String codPelicula = repoPeliculas.generarSiguienteCodigoPelicula();
                Pelicula p = new Pelicula(codPelicula, titulo, genero, clasificacion,duracion,RutaImagen,sinopsis);
                Sala s = new Sala(codFuncion, sala,20);
                Funcion nuevaFuncion = new Funcion(codFuncion, p, s, fecha, hora, precioFinal);
                
                repoPeliculas.guardarNuevaPelicula(p);           // 1ro Guardamos la película
                repoFunciones.guardarNuevaFuncion(nuevaFuncion);
                mostrarFunciones(repoFunciones.obtenerFunciones());
                    

                javax.swing.JOptionPane.showMessageDialog(null, "¡Película registrada con éxito!");
            }
        });
    }      
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VistaRegistroPeliculas.class.getName());
    
    public void mostrarFunciones(ArrayList<Funcion> funciones) {
        DefaultTableModel modelo = (DefaultTableModel) JTableRegistro.getModel();

        modelo.setRowCount(0);

        listaFunciones = new ArrayList<>(funciones);
 
        for (Funcion funcion : listaFunciones) {
            modelo.addRow(new Object[]{
                funcion.getCodigo(),
                funcion.getPelicula().getTitulo(),
                funcion.getPelicula().getGenero(),
                funcion.getPelicula().getClasificacion(),
                funcion.getSala().getNombre(),
                funcion.getFecha(),
                funcion.getHora(),
                String.format("$%.2f", funcion.getPrecioBase())
            });
        }

    }
    private void cargarDatosComboBox() {
    // 1. Limpiamos los items que vienen por defecto ("Item 1", "Item 2"...)
    CbClasificacion.removeAllItems();
    CbGenero.removeAllItems();
    CbSala.removeAllItems();
    
    // 2. Definimos nuestros ArrayLists o Arreglos con los datos
    String[] generos = {"Acción", "Comedia", "Drama", "Ciencia Ficción", "Terror","Aventura","Animacion"};
    String[] clasificaciones = {"Todo Público", "+12", "+16", "+18"};
    String[] salas = {"Sala 1", "Sala 2", "Sala 3", "Sala 4 (VIP)","Sala VIP"};
    
    // 3. Llenamos los ComboBox
    for (String genero : generos) {
        CbGenero.addItem(genero);
    }
    for (String clasificacion : clasificaciones) {
        CbClasificacion.addItem(clasificacion);
    }
    for (String sala : salas) {
        CbSala.addItem(sala);
    }
}
    
    
    /**
     * Creates new form VistaRegistroPeliculas
     */
    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(); 

        modelo.addColumn("Código");
        modelo.addColumn("Película");
        modelo.addColumn("Género");
        modelo.addColumn("Clasificación");
        modelo.addColumn("Sala");
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");
        modelo.addColumn("Precio");

        JTableRegistro.setModel(modelo);

       
        JTableRegistro.getTableHeader().setReorderingAllowed(false);
    }
    
   private void configurarArrastrarYSoltar() {
    lblNuevoPoster.setDropTarget(new DropTarget() {
        @Override
        public synchronized void drop(DropTargetDropEvent evt) {
            try {
                // 1. Le decimos al sistema que aceptamos que "copien" el archivo aquí
                evt.acceptDrop(DnDConstants.ACTION_COPY);
                
                // 2. Obtenemos los datos soltados
                List<File> files = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                
                if (!files.isEmpty()) {
                    File archivoImagen = files.get(0);
                    
                    // --- NUEVA LÓGICA: OBTENER EL TÍTULO Y GUARDAR ---
                    
                    // Obtenemos el texto del campo del nombre de la película
                    // (Asegúrate de que txtNombre sea el nombre correcto de tu JTextField)
                    String tituloPelicula = txtNombrePelicula.getText().trim(); 
                    
                    if (tituloPelicula.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Por favor, escriba el Nombre de la Película ANTES de arrastrar el póster.", "Falta el título", JOptionPane.WARNING_MESSAGE);
                        return; // Cortamos la ejecución para no guardar un archivo sin nombre
                    }
                    
                    // Extraer la extensión original del archivo (ej. ".jpg", ".png")
                    String nombreOriginal = archivoImagen.getName();
                    String extension = "";
                    int i = nombreOriginal.lastIndexOf('.');
                    if (i > 0) {
                        extension = nombreOriginal.substring(i);
                    }
                    
                    // Crear el nuevo nombre limpiando los espacios
                    String nombreSeguro = tituloPelicula.replaceAll("\\s+", "_");
                    String nuevoNombreArchivo = nombreSeguro + extension;
                    
                    // Definir la ruta de destino (src/recursos/posters)
                    Path carpetaDestino = Paths.get("src","main", "resources","imagenes","peliculas");
                    if (!Files.exists(carpetaDestino)) {
                        Files.createDirectories(carpetaDestino);
                    }
                    Path rutaFinal = carpetaDestino.resolve(nuevoNombreArchivo);
                    
                    // Copiar y renombrar el archivo físicamente en tu proyecto
                    Files.copy(archivoImagen.toPath(), rutaFinal, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Imagen guardada como: " + nuevoNombreArchivo);
                    
                    // Guardamos en la variable global EL NUEVO ARCHIVO YA RENOMBRADO
                    
                    posterSeleccionado = rutaFinal.toFile(); 
                    
                    
                    // 3. Procesamos la imagen visualmente
                    ImageIcon iconoOriginal = new ImageIcon(posterSeleccionado.getAbsolutePath());
                    Image imagen = iconoOriginal.getImage();
                    
                    int ancho = lblNuevoPoster.getWidth();
                    int alto = lblNuevoPoster.getHeight();
                    
                    // Verificamos que el label tenga tamaño para evitar errores
                    if (ancho > 0 && alto > 0) {
                        Image imagenEscalada = imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                        
                        lblNuevoPoster.setText(""); // Borramos el texto
                        lblNuevoPoster.setIcon(new ImageIcon(imagenEscalada)); // Ponemos la imagen
                        RutaImagen=nuevoNombreArchivo;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al soltar la imagen: " + e.getMessage());
                e.printStackTrace();
            }
        }
    });
}
   
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        panelEntradas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTableRegistro = new javax.swing.JTable();
        lblSubtitulo = new javax.swing.JLabel();
        txtNombrePelicula = new javax.swing.JTextField();
        lblUsuario = new javax.swing.JLabel();
        lblUsuario1 = new javax.swing.JLabel();
        CbGenero = new javax.swing.JComboBox<>();
        lblUsuario2 = new javax.swing.JLabel();
        CbClasificacion = new javax.swing.JComboBox<>();
        lblUsuario3 = new javax.swing.JLabel();
        CbSala = new javax.swing.JComboBox<>();
        lblUsuario4 = new javax.swing.JLabel();
        txtSinopsis = new javax.swing.JTextField();
        lblUsuario5 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        lblUsuario6 = new javax.swing.JLabel();
        txtHora = new javax.swing.JTextField();
        lblUsuario7 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        lblUsuario8 = new javax.swing.JLabel();
        lblNuevoPoster = new javax.swing.JLabel();
        btnCerra = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(10, 14, 20));

        lblLogo.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        lblLogo.setForeground(new java.awt.Color(218, 165, 32));
        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setText("CINEFIS");
        lblLogo.setPreferredSize(new java.awt.Dimension(300, 45));

        panelEntradas.setBackground(new java.awt.Color(18, 28, 38));
        panelEntradas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(218, 165, 32)));
        panelEntradas.setMinimumSize(new java.awt.Dimension(620, 360));

        JTableRegistro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(JTableRegistro);

        javax.swing.GroupLayout panelEntradasLayout = new javax.swing.GroupLayout(panelEntradas);
        panelEntradas.setLayout(panelEntradasLayout);
        panelEntradasLayout.setHorizontalGroup(
            panelEntradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEntradasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panelEntradasLayout.setVerticalGroup(
            panelEntradasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEntradasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblSubtitulo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblSubtitulo.setForeground(new java.awt.Color(240, 240, 240));
        lblSubtitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSubtitulo.setText("Ingreso de peliculas y funciones");

        txtNombrePelicula.setBackground(new java.awt.Color(10, 18, 26));
        txtNombrePelicula.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtNombrePelicula.setForeground(new java.awt.Color(240, 240, 240));
        txtNombrePelicula.setCaretColor(new java.awt.Color(240, 240, 240));
        txtNombrePelicula.setPreferredSize(new java.awt.Dimension(300, 38));
        txtNombrePelicula.addActionListener(this::txtNombrePeliculaActionPerformed);

        lblUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(218, 165, 32));
        lblUsuario.setText("Nombre Pelicula:");

        lblUsuario1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsuario1.setForeground(new java.awt.Color(218, 165, 32));
        lblUsuario1.setText("Género:");

        CbGenero.setBackground(new java.awt.Color(10, 18, 26));
        CbGenero.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        CbGenero.setForeground(new java.awt.Color(240, 240, 240));
        CbGenero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblUsuario2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsuario2.setForeground(new java.awt.Color(218, 165, 32));
        lblUsuario2.setText("Clasificación:");

        CbClasificacion.setBackground(new java.awt.Color(10, 18, 26));
        CbClasificacion.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        CbClasificacion.setForeground(new java.awt.Color(240, 240, 240));
        CbClasificacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblUsuario3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsuario3.setForeground(new java.awt.Color(218, 165, 32));
        lblUsuario3.setText("Sala:");

        CbSala.setBackground(new java.awt.Color(10, 18, 26));
        CbSala.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        CbSala.setForeground(new java.awt.Color(240, 240, 240));
        CbSala.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblUsuario4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsuario4.setForeground(new java.awt.Color(218, 165, 32));
        lblUsuario4.setText("Fecha: DD/MM/YYYY");

        txtSinopsis.setBackground(new java.awt.Color(10, 18, 26));
        txtSinopsis.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtSinopsis.setForeground(new java.awt.Color(240, 240, 240));
        txtSinopsis.setCaretColor(new java.awt.Color(240, 240, 240));
        txtSinopsis.setPreferredSize(new java.awt.Dimension(300, 38));
        txtSinopsis.addActionListener(this::txtSinopsisActionPerformed);

        lblUsuario5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsuario5.setForeground(new java.awt.Color(218, 165, 32));
        lblUsuario5.setText("Hora: 24h");

        txtFecha.setBackground(new java.awt.Color(10, 18, 26));
        txtFecha.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtFecha.setForeground(new java.awt.Color(240, 240, 240));
        txtFecha.setCaretColor(new java.awt.Color(240, 240, 240));
        txtFecha.setPreferredSize(new java.awt.Dimension(300, 38));
        txtFecha.addActionListener(this::txtFechaActionPerformed);

        lblUsuario6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsuario6.setForeground(new java.awt.Color(218, 165, 32));
        lblUsuario6.setText("Precio:");

        txtHora.setBackground(new java.awt.Color(10, 18, 26));
        txtHora.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtHora.setForeground(new java.awt.Color(240, 240, 240));
        txtHora.setCaretColor(new java.awt.Color(240, 240, 240));
        txtHora.setPreferredSize(new java.awt.Dimension(300, 38));
        txtHora.addActionListener(this::txtHoraActionPerformed);

        lblUsuario7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsuario7.setForeground(new java.awt.Color(218, 165, 32));
        lblUsuario7.setText("Sinopsis:");

        txtPrecio.setBackground(new java.awt.Color(10, 18, 26));
        txtPrecio.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtPrecio.setForeground(new java.awt.Color(240, 240, 240));
        txtPrecio.setCaretColor(new java.awt.Color(240, 240, 240));
        txtPrecio.setPreferredSize(new java.awt.Dimension(300, 38));
        txtPrecio.addActionListener(this::txtPrecioActionPerformed);

        lblUsuario8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsuario8.setForeground(new java.awt.Color(218, 165, 32));
        lblUsuario8.setText("Poster:");

        lblNuevoPoster.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNuevoPoster.setForeground(new java.awt.Color(240, 240, 240));
        lblNuevoPoster.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNuevoPoster.setText("Arrastre la imagen aqui");
        lblNuevoPoster.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(218, 165, 32)));

        btnCerra.setBackground(new java.awt.Color(218, 165, 32));
        btnCerra.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnCerra.setText("CERRAR SESIÓN");
        btnCerra.setFocusPainted(false);
        btnCerra.setMaximumSize(new java.awt.Dimension(144, 228));
        btnCerra.setMinimumSize(new java.awt.Dimension(144, 222));
        btnCerra.setPreferredSize(new java.awt.Dimension(144, 228));
        btnCerra.addActionListener(this::btnCerraActionPerformed);

        btnRegistrar.setBackground(new java.awt.Color(218, 165, 32));
        btnRegistrar.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnRegistrar.setText("REGISTRAR PELICULA");
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setMaximumSize(new java.awt.Dimension(144, 228));
        btnRegistrar.setMinimumSize(new java.awt.Dimension(144, 222));
        btnRegistrar.setPreferredSize(new java.awt.Dimension(144, 228));
        btnRegistrar.addActionListener(this::btnRegistrarActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(369, 369, 369))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblSubtitulo)
                        .addGap(410, 410, 410))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUsuario)
                            .addComponent(lblUsuario1)
                            .addComponent(lblUsuario2)
                            .addComponent(lblUsuario3)
                            .addComponent(lblUsuario4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNombrePelicula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CbGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CbClasificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblUsuario6)
                                        .addComponent(lblUsuario7))
                                    .addComponent(lblUsuario5, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CbSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtHora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSinopsis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblUsuario8, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(panelEntradas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addComponent(lblNuevoPoster, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(131, 131, 131))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCerra, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubtitulo)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombrePelicula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsuario)
                    .addComponent(lblUsuario5)
                    .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUsuario1)
                            .addComponent(CbGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUsuario2)
                            .addComponent(CbClasificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblUsuario7))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(CbSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(lblUsuario3)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblUsuario4)
                                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblUsuario8))
                                        .addGap(18, 18, 18)
                                        .addComponent(panelEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(68, 68, 68))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(283, 283, 283)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnCerra, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap(11, Short.MAX_VALUE))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblUsuario6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSinopsis, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblNuevoPoster, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombrePeliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombrePeliculaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombrePeliculaActionPerformed

    private void txtSinopsisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSinopsisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSinopsisActionPerformed

    private void txtFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaActionPerformed

    private void txtHoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoraActionPerformed

    private void txtPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioActionPerformed

    private void btnCerraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCerraActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrarActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new VistaRegistroPeliculas().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CbClasificacion;
    private javax.swing.JComboBox<String> CbGenero;
    private javax.swing.JComboBox<String> CbSala;
    private javax.swing.JTable JTableRegistro;
    private javax.swing.JButton btnCerra;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblNuevoPoster;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblUsuario1;
    private javax.swing.JLabel lblUsuario2;
    private javax.swing.JLabel lblUsuario3;
    private javax.swing.JLabel lblUsuario4;
    private javax.swing.JLabel lblUsuario5;
    private javax.swing.JLabel lblUsuario6;
    private javax.swing.JLabel lblUsuario7;
    private javax.swing.JLabel lblUsuario8;
    private javax.swing.JPanel panelEntradas;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtHora;
    private javax.swing.JTextField txtNombrePelicula;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtSinopsis;
    // End of variables declaration//GEN-END:variables
// Listener para el ComboBox de Género
    
    
}
