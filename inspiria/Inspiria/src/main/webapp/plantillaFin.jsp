
        <script>
        function mostrarPopup(){
            var popup=document.getElementById("popupPerfil");
            if(popup.style.display==="none"){
                popup.style.display="block";
            }else{
                popup.style.display="none";
            }
        }
        
        window.onclick=function(event){
            var popup = document.getElementById("popupPerfil");
            var perfil = document.querySelector(".contenedorPerfil");
            if(!perfil.contains(event.target)){
                popup.style.display="none";
            }
        }
        
        
        
        function avisoIdentificacion(){
            document.getElementById("avisoIdentificacion").style.display="block";
            document.getElementById("fondoOscuro").style.display="block";
        }
        
        function cerrarAvisoIdentificacion(){
            document.getElementById("avisoIdentificacion").style.display="none";
            document.getElementById("fondoOscuro").style.display="none";
        }
        
    </script>
</body>
</html>