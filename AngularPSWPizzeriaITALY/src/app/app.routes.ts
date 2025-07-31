import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: "carrello",
    loadComponent: () => import('./carrello/carrello.component').then((c) => c.CarrelloComponent)
  },
  {
    path: "",
    loadComponent: () => import('./home/home.component').then((c) => c.HomeComponent)
  },
  {
    path: "ordine",
    loadComponent: () => import('./ordine/ordine.component').then((c) => c.OrdineComponent)
  },
  {
    path: "cliente",
    loadComponent: () => import('./cliente/cliente.component').then((c) => c.ClienteComponent)
  },
  {
    path: "prodotto",
    loadComponent: () => import('./prodotto/prodotto.component').then((c) => c.ProdottoComponent)
  },
  { path: '**', redirectTo: '' },
];
