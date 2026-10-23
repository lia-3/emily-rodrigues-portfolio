from flask import Flask, render_template, request, redirect, url_for, session, flash, jsonify
from werkzeug.security import generate_password_hash, check_password_hash
from datetime import datetime
from decimal import Decimal
from extensions import db
from models import Usuario, Producto, CarritoItem, Pedido, PedidoItem

app = Flask(__name__)
app.secret_key = '2CpLr0AaVc!Eti2ca5'  
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+mysqlconnector://G14:G14%402025@localhost/tienda_online'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db.init_app(app)

# Middleware para cargar usuario en cada request
@app.before_request
def cargar_usuario():
    if 'usuario_id' in session:
        usuario = Usuario.query.get(session['usuario_id'])
        if usuario:
            request.usuario = usuario
            return
    request.usuario = None

# Helper para rutas protegidas
def login_required(f):
    def wrapper(*args, **kwargs):
        if not request.usuario:
            flash("Debes iniciar sesión para acceder a esta página")
            return redirect(url_for('login'))
        return f(*args, **kwargs)
    wrapper.__name__ = f.__name__
    return wrapper

# ----- Rutas de Autenticación -----
@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        email = request.form['email']
        password = request.form['password']
        usuario = Usuario.query.filter_by(email=email).first()
        
        if usuario and check_password_hash(usuario.contraseña_hash, password):
            session['usuario_id'] = usuario.id
            flash("Inicio de sesión exitoso")
            return redirect(url_for('inicio'))
        flash("Credenciales incorrectas")
    return render_template('login.html')

@app.route('/registro', methods=['GET', 'POST'])
def registro():
    if request.method == 'POST':
        nombre = request.form['nombre']
        email = request.form['email']
        password = request.form['password']
        
        if Usuario.query.filter_by(email=email).first():
            flash("Ya existe una cuenta con este email")
            return redirect(url_for('registro'))
            
        nuevo_usuario = Usuario(
            nombre=nombre,
            email=email,
            contraseña_hash=generate_password_hash(password)
        )
        db.session.add(nuevo_usuario)
        db.session.commit()
        
        flash("Registro exitoso. Por favor inicia sesión.")
        return redirect(url_for('login'))
    return render_template('registro.html')

@app.route('/logout')
def logout():
    session.pop('usuario_id', None)
    flash("Sesión cerrada correctamente")
    return redirect(url_for('inicio'))

# ----- Rutas Principales -----
@app.route('/')
def inicio():
    productos = Producto.query.limit(4).all()
    return render_template('inicio.html', productos_destacados=productos)

@app.route('/productos')
def mostrar_productos():
    productos = Producto.query.all()
    return render_template('productos.html', productos=productos)

# ----- Rutas de Carrito -----
@app.route('/carrito/agregar/<int:producto_id>')
@login_required
def agregar_al_carrito(producto_id):
    item = CarritoItem.query.filter_by(
        usuario_id=request.usuario.id,
        producto_id=producto_id
    ).first()

    if item:
        item.cantidad += 1
    else:
        item = CarritoItem(
            usuario_id=request.usuario.id,
            producto_id=producto_id,
            cantidad=1
        )
        db.session.add(item)
    
    db.session.commit()
    flash("Producto agregado al carrito")
    return redirect(url_for('mostrar_productos'))

@app.route('/carrito')
@login_required
def ver_carrito():
    items = CarritoItem.query.filter_by(usuario_id=request.usuario.id).all()
    total = sum(item.producto.precio * item.cantidad for item in items)
    return render_template('carrito.html', items=items, total=total)

@app.route('/carrito/eliminar/<int:item_id>')
@login_required
def eliminar_del_carrito(item_id):
    item = CarritoItem.query.get(item_id)
    if item and item.usuario_id == request.usuario.id:
        db.session.delete(item)
        db.session.commit()
        flash("Producto eliminado del carrito")
    return redirect(url_for('ver_carrito'))

# ----- Rutas de Pedidos -----
@app.route('/pedidos')
@login_required
def ver_pedidos():
    pedidos = Pedido.query.filter_by(usuario_id=request.usuario.id).order_by(Pedido.creado_en.desc()).all()
    return render_template('pedidos.html', pedidos=pedidos)

@app.route('/pedido/realizar')
@login_required
def realizar_pedido():
    items_carrito = CarritoItem.query.filter_by(usuario_id=request.usuario.id).all()
    
    if not items_carrito:
        flash("Tu carrito está vacío")
        return redirect(url_for('ver_carrito'))
    
    total = sum(item.producto.precio * item.cantidad for item in items_carrito)
    
    # Crear pedido
    nuevo_pedido = Pedido(
        usuario_id=request.usuario.id,
        total=total,
        estado='pendiente'
    )
    db.session.add(nuevo_pedido)
    db.session.flush()  # Para obtener el ID
    
    # Crear items del pedido
    for item in items_carrito:
        pedido_item = PedidoItem(
            pedido_id=nuevo_pedido.id,
            producto_id=item.producto_id,
            cantidad=item.cantidad,
            precio_unitario=item.producto.precio
        )
        db.session.add(pedido_item)
        db.session.delete(item)  # Eliminar del carrito
    
    db.session.commit()
    flash("Pedido realizado con éxito")
    return redirect(url_for('ver_pedidos'))

# ----- API REST -----
@app.route('/api/productos')
def api_productos():
    productos = Producto.query.all()
    return jsonify([{
        'id': p.id,
        'nombre': p.nombre,
        'precio': float(p.precio),
        'stock': p.stock,
        'img': p.img
    } for p in productos])

# ----- Inicialización -----
if __name__ == '__main__':
    with app.app_context():
        db.create_all()
        
        # Datos de prueba (opcional)
        if not Producto.query.first():
            productos_iniciales = [
                Producto(nombre="Camiseta", descripcion="Camiseta de algodón", precio=15.99, stock=100, img="img/cami.png"),
                Producto(nombre="Pantalón", descripcion="Pantalón jeans", precio=34.50, stock=50, img="img/pants.png"),
                Producto(nombre="Zapatillas", descripcion="Zapatillas running", precio=49.95, stock=30, img="img/shoes.png"),
                Producto(nombre="Gorra", descripcion="Gorra plana", precio=12.75, stock=80, img="img/cap.png"),
                Producto(nombre="Sudadera", descripcion="Sudadera con capucha", precio=29.99, stock=40, img="img/hoodie.png")
            ]
            db.session.add_all(productos_iniciales)
            
            # Usuario de prueba
            if not Usuario.query.first():
                usuario_test = Usuario(
                    nombre="Usuario Demo",
                    email="demo@test.com",
                    contraseña_hash=generate_password_hash("demo123")
                )
                db.session.add(usuario_test)
            
            db.session.commit()
    
    app.run(host='0.0.0.0', port=5000, debug=True)
