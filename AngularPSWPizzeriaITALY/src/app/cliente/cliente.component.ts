import { Component, OnInit } from '@angular/core';
import { ClienteService,cliente } from '../services/cliente.service';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cliente',
  templateUrl: './cliente.component.html',
  styleUrls: ['./cliente.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ]
})
export class ClienteComponent implements OnInit {

  cliente: any = { // Dati fittizi, sostituire con quelli reali
    nome: '',
    cognome: '',
    email: '',
    telefono: '',
    indirizzo:''
  };
  profileForm: FormGroup;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private clienteService: ClienteService) {
    this.profileForm = new FormGroup({
      telefono: new FormControl('', [
        Validators.pattern('^(\\+\\d{1,3}( )?)?\\d{10}$')
      ])
    });
  }

  ngOnInit(): void {
    this.loadUserData();
  }

  loadUserData(): void {
    this.clienteService.getCurrentUser().subscribe(
      (data) => {
        this.cliente = data;
        this.profileForm.patchValue({
          telefono: this.cliente.telefono
        });
      },
      (error) => {
        console.error('Errore nel caricamento dei dati utente:', error);
        this.errorMessage = 'Impossibile caricare i dati utente.';
      }
    );
  }

  onSubmit() {
    if (this.profileForm.valid) {
      const updatedData = this.profileForm.value;
      console.log('Dati inviati al backend:', updatedData); // Debug
      this.clienteService.updateUser(updatedData).subscribe(
        response => {
          console.log('Risposta dal backend:', response); // Debug
          this.successMessage = 'Profilo aggiornato con successo!';
        },
        error => {
          console.error('Errore durante l\'aggiornamento:', error); // Debug
          this.errorMessage = 'Errore durante l\'aggiornamento del profilo.';
        }
      );
    }
  }

  get telefono() {
    return this.profileForm.get('telefono');
  }
}
