import fitz  # PyMuPDF
import tkinter as tk
from tkinter import messagebox, filedialog
import re
import csv

class QuizApp:
    def __init__(self, root, pdf_path, csv_path):
        self.root = root
        self.root.title("Simulador de Examen - Resultados Pro")
        self.root.geometry("950x750")
        
        self.solutions = self.load_solutions(csv_path)
        self.questions = self.extract_questions(pdf_path)
        
        self.current_q = 0
        self.score = 0.0
        self.user_answers = [None] * len(self.questions)

        if not self.questions:
            messagebox.showerror("Error", "No se detectaron preguntas en el PDF.")
            root.destroy()
            return

        self.setup_ui()

    def load_solutions(self, csv_path):
        sols = {}
        try:
            with open(csv_path, mode='r', encoding='utf-8-sig') as f:
                reader = csv.DictReader(f)
                reader.fieldnames = [fn.lower().strip() for fn in reader.fieldnames]
                for row in reader:
                    p_key = next((k for k in row if 'preg' in k), None)
                    r_key = next((k for k in row if 'resp' in k), None)
                    if p_key and r_key:
                        num_str = re.search(r'\d+', row[p_key])
                        if num_str:
                            sols[int(num_str.group())] = row[r_key].strip().lower()[0]
        except Exception as e:
            print(f"Error CSV: {e}")
        return sols

    def extract_questions(self, path):
        doc = fitz.open(path)
        extracted = []
        current_q = None
        for page in doc:
            lines = page.get_text("text").split('\n')
            for line in lines:
                text = line.strip()
                if not text: continue
                q_match = re.match(r'^(\d+)\.\s*(.*)', text)
                if q_match:
                    if current_q: extracted.append(current_q)
                    current_q = {"num": int(q_match.group(1)), "text": q_match.group(2), "options": [], "correct_idx": None}
                elif current_q and re.match(r'^[a-d][\.\)]', text.lower()):
                    current_q["options"].append(text)
                    if text[0].lower() == self.solutions.get(current_q["num"]):
                        current_q["correct_idx"] = len(current_q["options"]) - 1
                elif current_q:
                    if not current_q["options"]: current_q["text"] += " " + text
                    else: current_q["options"][-1] += " " + text
        if current_q: extracted.append(current_q)
        doc.close()
        return extracted

    def setup_ui(self):
        self.root.configure(bg="#f0f2f5")
        self.info_frame = tk.Frame(self.root, pady=10, bg="#2c3e50")
        self.info_frame.pack(fill="x")
        self.label_score = tk.Label(self.info_frame, text="Puntos: 0.0", font=("Arial", 12, "bold"), bg="#2c3e50", fg="white")
        self.label_score.pack(side="right", padx=20)
        self.label_status = tk.Label(self.info_frame, text="", font=("Arial", 11), bg="#2c3e50", fg="white")
        self.label_status.pack(side="left", padx=20)

        self.main_frame = tk.Frame(self.root, padx=40, pady=20, bg="#f0f2f5")
        self.main_frame.pack(expand=True, fill="both")
        self.label_q = tk.Label(self.main_frame, text="", wraplength=800, font=("Arial", 13, "bold"), justify="left", bg="#f0f2f5")
        self.label_q.pack(pady=20, anchor="w")

        self.buttons = []
        for i in range(4):
            btn = tk.Button(self.main_frame, text="", wraplength=750, justify="left",
                            anchor="w", font=("Arial", 11), padx=20, pady=10, relief="flat",
                            command=lambda i=i: self.select_option(i))
            btn.pack(fill="x", pady=5)
            self.buttons.append(btn)

        self.nav_frame = tk.Frame(self.root, pady=20, bg="#f0f2f5")
        self.nav_frame.pack(fill="x")
        self.btn_prev = tk.Button(self.nav_frame, text="<< Anterior", command=self.prev_question, width=15)
        self.btn_prev.pack(side="left", padx=100)
        self.btn_next = tk.Button(self.nav_frame, text="Siguiente >>", command=self.next_question, width=15)
        self.btn_next.pack(side="right", padx=100)

        self.display_question()

    def display_question(self):
        q = self.questions[self.current_q]
        self.label_status.config(text=f"Pregunta {q['num']} ({self.current_q + 1}/{len(self.questions)})")
        self.label_q.config(text=q['text'])
        
        # Cambiar botón a "Terminar" si es la última
        if self.current_q == len(self.questions) - 1:
            self.btn_next.config(text="TERMINAR", bg="#3498db", fg="white", font=("Arial", 10, "bold"))
        else:
            self.btn_next.config(text="Siguiente >>", bg="#f8f9fa", fg="black", font=("Arial", 9))

        for i, btn in enumerate(self.buttons):
            if i < len(q["options"]):
                btn.config(text=q["options"][i], state="normal", bg="white", fg="#333")
                btn.pack(fill="x", pady=5)
                if self.user_answers[self.current_q] is not None: self.paint_buttons()
            else:
                btn.pack_forget()

    def select_option(self, idx):
        if self.user_answers[self.current_q] is not None: return
        q = self.questions[self.current_q]
        is_correct = (idx == q["correct_idx"])
        self.user_answers[self.current_q] = {"idx": idx, "correct": is_correct}
        
        # Lógica de puntos: +1 si acierta, -0.25 si falla
        self.score += 1.0 if is_correct else -0.25
        self.label_score.config(text=f"Puntos: {self.score:.2f}")
        self.paint_buttons()

    def paint_buttons(self):
        q = self.questions[self.current_q]
        ans = self.user_answers[self.current_q]
        for i, btn in enumerate(self.buttons):
            if i >= len(q["options"]): continue
            if i == q["correct_idx"]: btn.config(bg="#27ae60", fg="white", state="disabled")
            elif i == ans["idx"]: btn.config(bg="#c0392b", fg="white", state="disabled")
            else: btn.config(bg="#dcdde1", state="disabled")

    def show_stats(self):
        total = len(self.questions)
        # Contamos solo las que el usuario ha respondido realmente
        respondidas = len([a for a in self.user_answers if a is not None])
        correctas = len([a for a in self.user_answers if a is not None and a['correct']])
        incorrectas = respondidas - correctas
        no_contestadas = total - respondidas
        
        # Nota sobre 10: (Puntos obtenidos / Puntos posibles) * 10
        # Aseguramos que la nota no sea negativa
        puntos_finales = max(0, self.score)
        nota_sobre_10 = (puntos_finales / total) * 10

        stats_msg = (
            f"--- RESUMEN DEL EXAMEN ---\n\n"
            f"Preguntas totales: {total}\n"
            f"Respondidas: {respondidas}\n"
            f"No contestadas: {no_contestadas}\n\n"
            f"Correctas: {correctas}\n"
            f"Incorrectas: {incorrectas}\n\n"
            f"---------------------------\n"
            f"PUNTOS TOTALES: {puntos_finales:.2f}\n"
            f"NOTA FINAL: {nota_sobre_10:.2f} / 10"
        )
        messagebox.showinfo("Estadísticas Finales", stats_msg)
        self.root.destroy()

    def next_question(self):
        if self.current_q < len(self.questions) - 1:
            self.current_q += 1
            self.display_question()
        else:
            self.show_stats()

    def prev_question(self):
        if self.current_q > 0:
            self.current_q -= 1
            self.display_question()

if __name__ == "__main__":
    root = tk.Tk()
    root.withdraw()
    pdf = filedialog.askopenfilename(title="Selecciona el PDF")
    if pdf:
        csv_f = filedialog.askopenfilename(title="Selecciona el CSV")
        if csv_f:
            root.deiconify()
            app = QuizApp(root, pdf, csv_f)
            root.mainloop()