package uva.ssw.database;

import java.util.ArrayList;

import uva.ssw.database.daos.CategoriaDAO;
import uva.ssw.database.daos.ComentarioDAO;
import uva.ssw.database.daos.ComisionDAO;
import uva.ssw.database.daos.ContactoDAO;
import uva.ssw.database.daos.HerramientaDAO;
import uva.ssw.database.daos.MaterialDAO;
import uva.ssw.database.daos.PasoDAO;
import uva.ssw.database.daos.ProductoDAO;
import uva.ssw.database.daos.TematicaDAO;
import uva.ssw.database.daos.TutorialDAO;
import uva.ssw.database.daos.UsuarioDAO;
import uva.ssw.modelo.Categoria;
import uva.ssw.modelo.Comentario;
import uva.ssw.modelo.Comision;
import uva.ssw.modelo.Contacto;
import uva.ssw.modelo.Herramienta;
import uva.ssw.modelo.Material;
import uva.ssw.modelo.Paso;
import uva.ssw.modelo.Producto;
import uva.ssw.modelo.Tematica;
import uva.ssw.modelo.Tutorial;
import uva.ssw.modelo.Usuario;


public class ControladorDAOS {
    
    public static  Usuario loginPorCorreo(String correo, String password) {
        return UsuarioDAO.validarUsuario(correo, password);
    }

    public static Usuario loginPorNombre(String nombre, String password) {
        return UsuarioDAO.validarUsuario(nombre, password);
    }

    public static int registrarNuevoUsuario(Usuario usuario) {
        return UsuarioDAO.registrarUsuario(usuario);
    }

    
    public static boolean publicarContenidoCompleto(Tutorial t) {
        int idTutorial = TutorialDAO.insertarTutorial(t, t.getAutor().getIdentificador(), 
                            t.getCategoria().getIdCategoria(), t.getTematica().getIdTematica());

        if (idTutorial == -1) return false;
        t.setId(idTutorial);

        for (Paso p : t.getPasos()) {
            PasoDAO.insertarPaso(p, idTutorial); 
        }
        
        if (t.getMateriales() != null) {
            for (Material m : t.getMateriales()) {
                MaterialDAO.insertarMaterial(m, idTutorial);
            }
        }

        if (t.getHerramientas() != null) {
            for (Herramienta h : t.getHerramientas()) {
                HerramientaDAO.insertarHerramienta(h, idTutorial); 
            }
        }

        if (t.getProductoAsociado() != null) {
            Producto p = t.getProductoAsociado();
            return ProductoDAO.insertarProductoAsociadoTutorial(p, idTutorial, 
                         p.getCategoria().getIdCategoria(), p.getTematica().getIdTematica(), p.getAutor().getIdentificador());
        }

        return true;
    }
    

    public static boolean publicarProductoAislado(Producto p) {
        int idCategoria = p.getCategoria().getIdCategoria();
        int idTematica = p.getTematica().getIdTematica();
        int idUsuario = p.getAutor().getIdentificador();
        return ProductoDAO.insertarProductoAislado(p, idCategoria, idTematica, idUsuario);
    }
    
    
    public static boolean comentarTutorial(Comentario com, Tutorial tut) {
        int idUser = com.getAutor().getIdentificador();
        int idTutorial = tut.getId();
        return ComentarioDAO.publicarComentarioTutorial(com, idUser, idTutorial);
    }
    
    public static Categoria buscarCategoria(String nombre) {
        return CategoriaDAO.obtenerCategoriaPorNombre(nombre);
    }

    public static Tematica buscarTematica(String nombre) {
        return TematicaDAO.obtenerTematicaPorNombre(nombre);
    }

    public static Usuario buscarUsuario(String nombre) {
        return UsuarioDAO.obtenerUsuarioPorNombre(nombre);
    }
    
    public static ArrayList<String> listarTodosLosNombresDeCategorias() {
       return CategoriaDAO.obtenerNombreTodasCategoriasExistentes();
    }
    
    public static ArrayList<String> listarTodosLosNombresDeTematicas() {
        return TematicaDAO.obtenerNombreTodasTematicasExistentes();
    }
    
    
    public static Usuario obtenerPerfilPropio(Usuario usuarioLoggeado) {
        Usuario dbData = UsuarioDAO.obtenerPerfilUsuario(usuarioLoggeado.getIdentificador());
        if (dbData != null) {
            usuarioLoggeado.setDescripcion(dbData.getDescripcion());
            usuarioLoggeado.setFotoPerfil(dbData.getFotoPerfil());
            usuarioLoggeado.setFotoBase64(dbData.getFotoBase64()); 
            usuarioLoggeado.setAceptaComisiones(dbData.isAceptaComisiones());
        }
        usuarioLoggeado.setProductosEnVenta(
            ProductoDAO.obtenerProductosPorUsuario(usuarioLoggeado.getIdentificador()));
        usuarioLoggeado.setTutorialesPublicados(
            TutorialDAO.obtenerTutorialesPorUsuario(usuarioLoggeado.getIdentificador()));
        return usuarioLoggeado;
    }
    
    public static Usuario obtenerDatosConfiguracionPerfil(Usuario usuarioLoggeado) {
        return UsuarioDAO.obtenerPerfilUsuario(usuarioLoggeado.getIdentificador());
    }

    public static boolean actualizarNombre(int identificador, String trim) {
        return UsuarioDAO.actualizarNombre(identificador, trim);
    }

    public static boolean comprobarContrasenaActual(int identificador, String passActual) {
        return UsuarioDAO.comprobarContrasenaActual(identificador, passActual);
    }

    public static boolean actualizarPassword(int identificador, String passNueva) {
        return UsuarioDAO.actualizarPassword(identificador, passNueva);
    }

    public static boolean actualizarDescripcion(int identificador, String nuevaDesc) {
        return UsuarioDAO.actualizarDescripcion(identificador, nuevaDesc);
    }

    public static boolean actualizarFotoPerfil(int identificador, byte[] foto) {
        return UsuarioDAO.actualizarFotoPerfil(identificador, foto);
    }

    public static Usuario validarUsuario(String email, String contrasenna) {
        return UsuarioDAO.validarUsuario(email, contrasenna);
    }

    public static int registrarUsuario(Usuario nuevoUsuario) {
        return UsuarioDAO.registrarUsuario(nuevoUsuario);
    }
    
    public static Tutorial obtenerTutorialCompleto(int idTutorial) {
        Tutorial t = TutorialDAO.obtenerTutorial(idTutorial); 

        if (t != null) {
            t.setPasos(PasoDAO.obtenerPasosTutorial(idTutorial));
            t.setMateriales(MaterialDAO.obtenerListaMateriales(idTutorial));
            t.setHerramientas(HerramientaDAO.obtenerListaHerramientas(idTutorial));
            t.setComentarios(ComentarioDAO.obtenerListaComentariosTutorial(idTutorial));
        }
        return t;
    }
    
    public static Producto obtenerProductoCompleto(int idProducto){
        return ProductoDAO.obtenerProducto(idProducto);
    }
    
    public static ArrayList<Tutorial> obtenerTodosTutoriales() {
        return TutorialDAO.obtenerTodosTutoriales();
    }
    
    public static ArrayList<Tutorial> obtenerTutorialesGuardados(int idUsuario) {
        return TutorialDAO.obtenerTutorialesGuardados(idUsuario);
    }

    public static boolean guardarTutorial(int idUser, int idTut) {
        return TutorialDAO.guardarTutorial(idUser, idTut);
    }

    public static boolean esTutorialGuardado(int idUser, int idTut) {
        return TutorialDAO.esTutorialGuardado(idUser, idTut);
    }
    
    public static boolean eliminarTutorialGuardado(int idUser, int idTut) {
        return TutorialDAO.eliminarTutorialGuardado(idUser, idTut);
    }
    
    
    public static ArrayList<Comision> obtenerComisionesUsuario(int idUsuario) {
        return ComisionDAO.obtenerComisionesPorUsuario(idUsuario);
    }

    public static boolean guardarComisiones(int idUsuario, ArrayList<Comision> lista, boolean aceptaComisiones) {
        UsuarioDAO.actualizarAceptaComisiones(idUsuario, aceptaComisiones); 
        return ComisionDAO.actualizarComisiones(idUsuario, lista);
    }
    
    
    public static boolean actualizarEstadoComisionesSolo(int idUsuario, boolean aceptaComisiones) {
        return UsuarioDAO.actualizarAceptaComisiones(idUsuario, aceptaComisiones); 
    }
    
    
    public static ArrayList<Contacto> obtenerContactosUsuario(int idUsuario) {
        return ContactoDAO.obtenerContactosPorUsuario(idUsuario);
    }

    public static boolean guardarContactos(int idUsuario, ArrayList<Contacto> lista) {
        return ContactoDAO.actualizarContactos(idUsuario, lista);
    }
    
    public static Usuario obtenerPerfilCompletoPorId(int idUsuario) {
        Usuario usuario = UsuarioDAO.obtenerPerfilExterno(idUsuario);

        if (usuario != null) {
            usuario.setProductosEnVenta(ProductoDAO.obtenerProductosPorUsuario(idUsuario));
            usuario.setTutorialesPublicados(TutorialDAO.obtenerTutorialesPorUsuario(idUsuario));
            usuario.setContactos(ContactoDAO.obtenerContactosPorUsuario(idUsuario));
            usuario.setListaComisiones(ComisionDAO.obtenerComisionesPorUsuario(idUsuario));
        }

        return usuario;
    }
    
    public static boolean seguirUsuario(int idSigue, int idSeguido) { 
        return UsuarioDAO.seguirUsuario(idSigue, idSeguido); 
    }
    
    public static boolean dejarSeguirUsuario(int idSigue, int idSeguido) { 
        return UsuarioDAO.dejarSeguirUsuario(idSigue, idSeguido); 
    }
    
    public static boolean esPerfilSeguido(int idSigue, int idSeguido) { 
        return UsuarioDAO.esPerfilSeguido(idSigue, idSeguido); 
    }
    
    public static ArrayList<Usuario> obtenerPerfilesSeguidos(int idUsuario) {
        return UsuarioDAO.obtenerPerfilesSeguidos(idUsuario);
    }
    
    
    public static boolean seguirCategoria(int idU, int idC) { 
        return CategoriaDAO.seguirCategoria(idU, idC); 
    }
    
    public static boolean dejarSeguirCategoria(int idU, int idC) { 
        return CategoriaDAO.dejarSeguirCategoria(idU, idC); 
    }
    
    public static boolean esCategoriaSeguida(int idU, int idC) { 
        return CategoriaDAO.esCategoriaSeguida(idU, idC); 
    }
    
    public static ArrayList<Categoria> obtenerCategoriasSeguidas(int idUsuario) { 
        return CategoriaDAO.obtenerCategoriasSeguidas(idUsuario); 
    }
    
    public static boolean seguirTematica(int idU, int idT) { 
        return TematicaDAO.seguirTematica(idU, idT); 
    }
    
    public static boolean dejarSeguirTematica(int idU, int idT) { 
        return TematicaDAO.dejarSeguirTematica(idU, idT); 
    }
    
    public static boolean esTematicaSeguida(int idU, int idT) { 
        return TematicaDAO.esTematicaSeguida(idU, idT); 
    }
    
    public static ArrayList<Tematica> obtenerTematicasSeguidas(int idUsuario) { 
        return TematicaDAO.obtenerTematicasSeguidas(idUsuario); 
    }
    
    public static ArrayList<Tutorial> obtenerTutorialesMuroFiltrado(int idUsuarioLogueado) {
        return TutorialDAO.obtenerTutorialesMuroFiltrado(idUsuarioLogueado);
    }
    
    public static boolean eliminarTutorial(int idUsuario, int idTutorial){
        return TutorialDAO.eliminarTutorial(idUsuario,idTutorial);
    }
    
    public static boolean eliminarProducto(int idUsuario, int idProducto){
        return ProductoDAO.eliminarProducto(idUsuario,idProducto);
    }
    
    public static ArrayList<Tutorial> obtenerTutorialesPorCategoria(int idCategoria) {
        return uva.ssw.database.daos.TutorialDAO.obtenerTutorialesPorCategoria(idCategoria);
    }
    
    public static ArrayList<Tutorial> obtenerTutorialesPorTematica(int idTematica) {
        return uva.ssw.database.daos.TutorialDAO.obtenerTutorialesPorTematica(idTematica);
    } 
}