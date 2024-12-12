import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CourService } from '../../services/cour.service';
import { Cour } from '../../Modeles/cour';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Enseignant } from '../../Modeles/enseignant';


/* interface Cour {
  id: number;
  titre: string;
  description: string;
  dateDebut:Date;
  dateFin:Date
} */

@Component({
  selector: 'app-cour',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cour.component.html',
  styleUrls: ['./cour.component.css']
})
export class CourComponent implements OnInit {
  cours: Cour[] = [];
  courForm: FormGroup;
  updateForm: FormGroup;
  tot: number;
  searchText: string;
  userconnect = JSON.parse(localStorage.getItem('access_token')!);
  constructor(private fb: FormBuilder, private serviceCour: CourService) {
  }

  ngOnInit(): void {
    this.createCour()
    this.updatedCour()
    this.getAllCourses()
    this.tot = this.cours.length;
  }
  // getAllCourses(): void {
  //   this.serviceCour.getAllCours().subscribe((res: any) => {
  //     this.cours = res
  //     console.log("liste des cours", this.cours);
  //   });
  // }
  getAllCourses(): void {
    this.serviceCour.getAllCours().subscribe((res: any) => {
      this.cours = res.filter(
        (el: any) =>{
          return(
         el.enseignantId.id ==this.userconnect.id
          )
     } );
      console.log("Liste des cours :", this.cours);
    }, (error) => {
      console.error("Erreur lors de la récupération des cours :", error);
    });
  }

  createCour(): void {
    this.courForm = this.fb.group({
      titre: ['', Validators.required],
      description: ['', Validators.required],
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required]
    });
  }
  updatedCour(): void {
    this.updateForm = this.fb.group({
      id: ['', Validators.required],
      titre: ['', Validators.required],
      description: ['', Validators.required],
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required]
    });
  }
  openAddModal(): void {

  }

  onExportPdf(): void {

  }

  search(): void {

  }

  editCour(cour: Cour): void {

  }

  /*   deleteCour(id: number): void {
      this.cours = this.cours.filter(c => c.id !== id);
      this.showNotification('Cours supprimé avec succès!', 'danger');
    } */

  addCour(): void {
    if (this.courForm.valid) {
      const newCour: Cour = { ...this.courForm.value, id: this.cours.length + 1 };
      this.cours.push(newCour);
      this.showNotification('Cours ajouté avec succès!', 'success');
      this.courForm.reset();
    }
  }

  /* updateCour(): void {
    if (this.updateForm.valid) {
      const updatedCour: Cour = this.updateForm.value;
      const index = this.cours.findIndex(c => c.id === updatedCour.id);
      if (index !== -1) {
        this.cours[index] = updatedCour;
        this.showNotification('Cours mis à jour avec succès!', 'success');
        this.updateForm.reset();
      }
    }
  } */

  showNotification(message: string, type: string): void {
    const notification = document.createElement('div');
    notification.className = `alert alert-${type}`;
    notification.innerText = message;
    document.body.appendChild(notification);
    setTimeout(() => {
      document.body.removeChild(notification);
    }, 3000);
  }

}
