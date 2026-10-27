;;;;;;;;;; Autores: Antolín Caramazana, Angel. Rodrigues Álvarez, Emily Fabiana. ;;;;;;;;;;


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;           Plantillas para o-a-v multivaluados y univaluados           ;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(deftemplate oav-m "Plantilla para hechos multivaluados"
	(slot objeto (type SYMBOL))
	(slot atributo (type SYMBOL))
	(slot valor))

(deftemplate oav-u "Plantilla para hechos univaluados"
	(slot objeto (type SYMBOL))
	(slot atributo (type SYMBOL))
	(slot valor))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;             Hechos iniciales sobre 2 pacientes diferentes             ;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(deffacts pacientes-univaluado "Datos univaluados de los pacientes"
	(oav-u (objeto Carolina)
		(atributo edad)
		(valor 80))
	(oav-u (objeto Carolina)
		(atributo sexo)
		(valor Mujer))
	(oav-u (objeto Carolina)
		(atributo aniosFumando)
		(valor 20))
	(oav-u (objeto Carolina)
		(atributo presionSistolica)
		(valor 120))
	(oav-u (objeto Carolina)
		(atributo presionDiastolica)
		(valor 75))
	(oav-u (objeto Francisco)
		(atributo edad)
		(valor 68))
	(oav-u (objeto Francisco)
		(atributo sexo)
		(valor Hombre))
	(oav-u (objeto Francisco)
		(atributo aniosFumando)
		(valor 0))
	(oav-u (objeto Francisco)
		(atributo presionSistolica)
		(valor 155))
	(oav-u (objeto Francisco)
		(atributo presionDiastolica)
		(valor 95))
)

(deffacts pacientes-multivaluados "Sintomas y pruebas de los pacientes"
	(oav-m (objeto Carolina)
		(atributo sintoma)
		(valor calambresPiernas))
	(oav-m (objeto Carolina)
		(atributo sintoma)
		(valor febricula))
	(oav-m (objeto Carolina)
		(atributo evidencia)
		(valor rumorSistolico))
	(oav-m (objeto Francisco)
		(atributo sintoma)
		(valor dolorPecho))
	(oav-m (objeto Francisco)
		(atributo evidencia)
		(valor dilatacionCorazon))
	(oav-m (objeto Francisco)
		(atributo evidencia)
		(valor masaPulsanteAbdomen))
)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;                       Reglas de mayor prioridad                       ;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrule eliminar-hecho-univaluado "Elimina univaluados duplicados"
	(declare (salience 1000))
	?x <- (oav-u (objeto ?objeto) (atributo ?atributo) (valor ?valor1))
	?y <- (oav-u (objeto ?objeto) (atributo ?atributo) (valor ?valor2))
	(test (neq ?valor1 ?valor2))
	(test (< (fact-index ?x) (fact-index ?y)))
	=>
	(retract ?x)
)

(defrule calcular-presion-pulso "Calcula la presion de pulso del paciente"
	(declare (salience 10))
	(oav-u (objeto ?paciente) (atributo presionSistolica) (valor ?presion1))
	(oav-u (objeto ?paciente) (atributo presionDiastolica) (valor ?presion2))
	=>
	(bind ?presionPulso (- ?presion1 ?presion2))
	(assert (oav-u (objeto ?paciente) (atributo presionPulso) (valor ?presionPulso)))
)

(defrule grupo-de-riesgo "Comprueba si el paciente pertenece a un grupo de riesgo"
	(declare (salience 10))
	(or (oav-u (objeto ?paciente) (atributo obesidad) (valor Si))
		(oav-u (objeto ?paciente) (atributo aniosFumando) (valor ?anios & :(>= ?anios 10)))
		(oav-u (objeto ?paciente) (atributo edad) (valor ?edad & :(>= ?edad 65))))
	=>
	(assert (oav-u (objeto ?paciente) (atributo grupoDeRiesgo) (valor Si)))
)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;                      Diagnostico de enfermedades                      ;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrule aneurisma-arteria-abdominal "Diagnostica aneurisma en la arteria abdominal"
	(oav-m (objeto ?paciente) (atributo sintoma) (valor dolorAbdominal))
	(oav-m (objeto ?paciente) (atributo evidencia) (valor rumorAbdominal))
	(oav-m (objeto ?paciente) (atributo evidencia) (valor masaPulsanteAbdomen))
	=>
	(assert (oav-m (objeto ?paciente) (atributo diagnostico) (valor aneurisma-arteria-abdominal)))
	(printout t "El paciente " ?paciente " sufre aneurisma de la arteria abdominal, enfermedad de tipo cardiovascular que afecta a los vasos sanguineos" crlf)
)

(defrule regurgitacion-aortica "Diagnostica regurgitacion aortica"
	(oav-u (objeto ?paciente) (atributo presionSistolica) (valor ?presionSistolica & :(>= ?presionSistolica 150)))
	(oav-u (objeto ?paciente) (atributo presionPulso) (valor ?presionPulso & :(>= ?presionPulso 65)))
	(or (oav-m (objeto ?paciente) (atributo evidencia) (valor rumorSistolico))
		(oav-m (objeto ?paciente) (atributo evidencia) (valor dilatacionCorazon)))
	=>
	(assert (oav-m (objeto ?paciente) (atributo diagnostico) (valor regurgitacion-aortica)))
	(printout t "El paciente " ?paciente " sufre regurgitacion aortica, enfermedad de tipo cardiovascular que afecta al corazon" crlf)
)

(defrule estenosis-arteria-pierna "Diagnostica estenosis en arteria"
	(oav-m (objeto ?paciente) (atributo sintoma) (valor calambresPiernas))
	=>
	(assert (oav-m (objeto ?paciente) (atributo diagnostico) (valor estenosis-arteria-pierna)))
	(printout t "El paciente " ?paciente " sufre estenosis en una arteria de las piernas, enfermedad de tipo cardiovascular que afecta a los vasos sanguineos" crlf)
)

(defrule arteriosclerosis "Diagnostica arteriosclerosis"
	(oav-m (objeto ?paciente) (atributo diagnostico) (valor estenosis-arteria-pierna)) 
	(oav-u (objeto ?paciente) (atributo grupoDeRiesgo) (valor Si))
	=>
	(assert (oav-m (objeto ?paciente) (atributo diagnostico) (valor arteriosclerosis)))
	(printout t "El paciente " ?paciente " sufre arteriosclerosis, enfermedad de tipo cardiovascular que afecta a los vasos sanguineos" crlf)
)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;                    No diagnostico de enfermedades                     ;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    
(defrule diagnostico-sin-asignar "Informa si no se asigna ningun diagnostico"
	(declare (salience -10))
	(oav-u (objeto ?paciente) (atributo edad) (valor ?))
	(not (oav-m (objeto ?paciente) (atributo diagnostico) (valor ?enfermedad)))
	=>
	(printout t "Al paciente " ?paciente " no se le ha asignado ningun diagnostico de enfermedad cardiovascular." crlf)
)
