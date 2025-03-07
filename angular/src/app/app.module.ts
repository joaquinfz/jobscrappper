import { AppRoutingModule } from './app-routing.module';  // Import it here

@NgModule({
declarations: [
AppComponent,
JobListComponent,
JobDetailComponent
],
imports: [
AppRoutingModule  // Add it to the imports array
  ],
bootstrap: [AppComponent]
})
export class AppModule {}
