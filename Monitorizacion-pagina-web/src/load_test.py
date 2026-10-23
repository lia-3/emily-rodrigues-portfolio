from locust import HttpUser, task, between, constant
from faker import Faker
import random

fake = Faker()

class QuickstartUser(HttpUser):
    wait_time = between(1, 2)
    host = "http://localhost:5000"
    
    def __init__(self, parent):
        super().__init__(parent)
        self.productos = []
        self.auth_token = None
    
    def on_start(self):
        # Obtener lista de productos disponibles
        response = self.client.get("/api/productos")
        self.productos = response.json()
        
        # Autenticar o registrar usuario
        if random.random() < 0.7:
            self.login_existing_user()
        else:
            self.register_new_user()
    
    def login_existing_user(self):
        # Usuario de prueba creado en app.py
        response = self.client.post("/login", {
            "email": "demo@test.com",
            "password": "demo123"
        })
        if response.status_code != 200:
            self.logout()
    
    def register_new_user(self):
        user_data = {
            "nombre": fake.name(),
            "email": fake.email(),
            "password": fake.password()
        }
        self.client.post("/registro", data=user_data)
        self.login(user_data["email"], user_data["password"])
    
    def login(self, email, password):
        response = self.client.post("/login", {
            "email": email,
            "password": password
        })
        return response.status_code == 200

    @task(5)
    def browse_products(self):
        self.client.get("/")
        self.client.get("/productos")
        self.client.get("/api/productos")
    
    @task(3)
    def full_purchase_flow(self):
        if not self.productos:
            return
        
        # Seleccionar producto aleatorio
        producto = random.choice(self.productos)
        
        # Añadir al carrito
        self.client.get(f"/carrito/agregar/{producto['id']}")
        
        # Ver carrito
        self.client.get("/carrito")
        
        # Realizar pedido (solo el 50% de las veces)
        if random.random() < 0.5:
            self.client.get("/pedido/realizar")
            self.client.get("/pedidos")
    
    @task(2)
    def user_registration_flow(self):
        user_data = {
            "nombre": fake.name(),
            "email": fake.email(),
            "password": fake.password()
        }
        self.client.post("/registro", data=user_data)
        self.login(user_data["email"], user_data["password"])
    
    @task(1)
    def view_orders(self):
        self.client.get("/pedidos")
    
    @task(1)
    def manage_cart(self):
        self.client.get("/carrito")
        if self.productos and random.random() < 0.3:
            producto = random.choice(self.productos)
            self.client.get(f"/carrito/agregar/{producto['id']}")
