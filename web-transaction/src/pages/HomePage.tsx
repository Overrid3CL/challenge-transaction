import { DashboardCards } from "@/components/transactions/DashboardCards"

export function HomePage() {
  return (
    <div className="container mx-auto px-4 py-8  gap-2 md:gap-5 flex flex-col">
      <h1 className="mb-4 text-3xl font-bold">Inicio</h1>
      <p className="text-muted-foreground">
        Bienvenido a Web Transaction
      </p>
      <DashboardCards />
    </div>
  )
}
