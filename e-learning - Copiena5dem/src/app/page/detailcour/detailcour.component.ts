import { Component, OnInit } from '@angular/core';
import { CourService } from '../../services/cour.service';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Cour } from '../../Modeles/cour';

@Component({
  selector: 'app-detailcour',
  standalone: true,
  imports: [CommonModule,RouterLink],
  templateUrl: './detailcour.component.html',
  styleUrl: './detailcour.component.css'
})
export class DetailcourComponent implements OnInit {
id= this.activatedroute.snapshot.params['id'];
// courId: Cour[] = [];
courId:any
constructor(private courService:CourService,private activatedroute:ActivatedRoute){}

ngOnInit(): void {
  console.log('id', this.id);  
  this.getCourById()
  
}
getCourById(): void{            
  this.courService.getCourById(this.id).subscribe((res: any)=>{
    this.courId = res          
    console.log("cours by id",this.courId);           
    
  })   
}
}
