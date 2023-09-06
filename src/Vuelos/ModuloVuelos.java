package Vuelos;

import Principal.Login;
import Reservas.ReservaAsiento;
import Vuelos.Logica.*;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ModuloVuelos extends JFrame{
    private JButton mostrarCatalogoButton;
    public JPanel plnPrincipalVuelos;
    private JTextField origen;
    private JTextField destino;
    private JTable table1;
    private JPanel pnlListaVuelos;
    private JPanel pnlCalendario;
    private JLabel lblFecha;
    private JButton buscarVuelosButton;
    private JButton btnSeleccionarVuelo;
    private JTabbedPane tabbedPane1;
    private JButton confirmarButton;
    private JButton eliminarButton;
    private JTable table2;
    private JButton btnModificarReserva;
    private JButton cambiarReservaButton;
    private GestorVuelos g = new GestorVuelos();
    private GestorReservasAsiento gestorReservasAsiento = new GestorReservasAsiento();
    private SelectorDeAsientos selectorDeAsientos;
    private JDateChooser dateChooserInicio = new JDateChooser();
    private ReservaAsiento asientoReservado;
    private PagoVuelos pagoVuelos = new PagoVuelos();
    private CarritoAsientos carritoAsientos;
    private  Vuelo v;
    private JDialog dialog ;
    private Login login;



    public ModuloVuelos(Login login){
        this.login = login;

        selectorDeAsientos = new SelectorDeAsientos(this);
        pnlListaVuelos.setVisible(true);
        mostrarCatalogoButton.addActionListener(e -> pnlListaVuelos.setVisible(true));
        //regresarButton.addActionListener(e -> {
        //    Módulos módulos = new Módulos(login);
        //    módulos.crearFrame();
        //    dispose();
        //});


        //salirButton.addActionListener(e -> {
        //    dispose();
        //    System.exit(0);
        //});

        //calendario
        pnlCalendario.add(dateChooserInicio);
        buscarVuelosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.mostarVuelosFiltrados(table1, g.buscarVuelo(origen.getText(), destino.getText()));
            }
        });
        mostrarCatalogoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MostrarTabla();

            }
        });
        btnSeleccionarVuelo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(v != null) {
                    if(VerificarReserva()) {
                        selectorDeAsientos.setVuelo(g.seleccionarVuelo(v));
                        mostrarPantallaEmergente(ModuloVuelos.this);
                    } else {
                        JOptionPane.showMessageDialog(null, "Ya existe un reserva con ese vuelo", "Aviso", JOptionPane.ERROR_MESSAGE);
                    }
                } else{
                    JOptionPane.showMessageDialog(null, "Seleccione un vuelo", "Aviso", JOptionPane.ERROR_MESSAGE);

                }
                //selectorDeAsientos.crearframe();
                //setPanel(selectorDeAsientos.);
                //dispose();
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = table1.getSelectedRow();
                if (fila != -1) {
                    v = new Vuelo(table1.getValueAt(fila, 0).toString(),
                            table1.getValueAt(fila, 1).toString(),
                            table1.getValueAt(fila, 3).toString(),
                            table1.getValueAt(fila, 2).toString(),
                            Integer.parseInt(table1.getValueAt(fila, 4).toString()));
                }
            }
        });
        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectorDeAsientos.setVuelo(g.seleccionarVuelo(v));
                mostrarPagoVuelos(ModuloVuelos.this);
                if(asientoReservado != null) {
                    selectorDeAsientos.setVuelo(g.seleccionarVuelo(v));
                    mostrarPagoVuelos(ModuloVuelos.this);
                } else{
                    JOptionPane.showMessageDialog(null, "Previamente debe reservar su/s asiento/s", "Aviso", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(asientoReservado != null) {
                    JOptionPane.showMessageDialog(null, "Su reserva ha sido eliminada", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                } else{
                    JOptionPane.showMessageDialog(null, "Previamente debe reservar su/s asiento/s", "Aviso", JOptionPane.ERROR_MESSAGE);

                }

            }
        });
        cambiarReservaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pagoVuelos.setAsiento(selectorDeAsientos.getCarrito());
                mostrarPagoVuelos(ModuloVuelos.this);

                if(asientoReservado != null) {
                    selectorDeAsientos.setVuelo(g.seleccionarVuelo(v));
                    mostrarSelectorDeAsiento(ModuloVuelos.this);
                } else{
                    JOptionPane.showMessageDialog(null, "Seleccione un vuelo", "Aviso", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        btnModificarReserva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        table2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
    }

    public void crearframe() {
        setTitle("Modulo Vuelos");
        setSize(1000, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void mostrarPantallaEmergente(JFrame parentFrame) {
        this.dialog = new JDialog(parentFrame, "Pantalla Emergente", true);

        // Configurar el contenido de la pantalla emergente
        //JLabel label = new JLabel("Esto es una pantalla emergente.");

        dialog.add(selectorDeAsientos.pnlPrincipalAsientos);

        // Configurar el tamaño de la pantalla emergente
        dialog.setSize(804, 604);

        // Centrar la pantalla emergente en la ventana principal
        dialog.setLocationRelativeTo(parentFrame);

        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Hacer visible la pantalla emergente
        dialog.setVisible(true);


    }

    public void cerrarDialog(){
        dialog.dispose();
        actualizar();
    }


    public void MostrarTabla(){
        g.mostrarVuelos(table1);
    }
    private void mostrarPagoVuelos(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Pantalla Emergente", true);
        dialog.add(pagoVuelos.JPPagoVuelos);
        dialog.setSize(804, 604);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    private void mostrarSelectorDeAsiento(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Pantalla Emergente", true);
        dialog.add(selectorDeAsientos.pnlPrincipalAsientos);
        dialog.setSize(804, 604);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }


    public void actualizar(){
        gestorReservasAsiento.mostrarReservas(table2);

    }


    public void crearReserva(CarritoAsientos carrito) {
        ReservaAsiento reservaAsiento = new ReservaAsiento(login, carrito);
        reservaAsiento.reservar();
        gestorReservasAsiento.agregarResarva(reservaAsiento);
    }

    public boolean VerificarReserva(){
        ComparadorVuelo com = new ComparadorVuelo();
        for (int i = 0; i < table2.getRowCount(); i++) {
            Vuelo aux = new Vuelo(table2.getValueAt(i,1).toString(),
                    table2.getValueAt(i,2).toString(),
                    table2.getValueAt(i,3).toString(),
                    table2.getValueAt(i,4).toString());
            if(com.compare(aux, this.v) == 1 ){
                return false;
            }
        }
        return true;
    }
}

