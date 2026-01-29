import { useState, useEffect } from "react";
import { toast } from "sonner";
import { Card, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { transactionService } from "@/lib/api/transactionService";
import type { TransactionStatsDTO } from "@/types/transaction";

/**
 * Formatea un número como moneda en formato USD
 */
function formatCurrency(value: number): string {
  return new Intl.NumberFormat("es-CL", {
    style: "currency",
    currency: "CLP",
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(value);
}

/**
 * Formatea un número con separadores de miles
 */
function formatNumber(value: number): string {
  return new Intl.NumberFormat("es-CL").format(value);
}

export function DashboardCards() {
  const [stats, setStats] = useState<TransactionStatsDTO | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadStatistics();
  }, []);

  const loadStatistics = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await transactionService.getStatistics();
      setStats(data);
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || err.message || "Error al cargar las estadísticas";
      setError(errorMessage);
      toast.error(errorMessage);
      console.error("Error al cargar estadísticas:", err);
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) {
    return (
      <div className="grid grid-cols-1 gap-4 *:data-[slot=card]:shadow-xs lg:grid-cols-4">
        {[1, 2, 3, 4].map((i) => (
          <Card key={i} className="@container/card">
            <CardHeader>
              <CardDescription className="h-4 w-24 animate-pulse bg-muted rounded" />
              <CardTitle className="h-8 w-32 animate-pulse bg-muted rounded mt-2" />
            </CardHeader>
          </Card>
        ))}
      </div>
    );
  }

  if (error) {
    return (
      <div className="grid grid-cols-1 gap-4">
        <Card>
          <CardHeader>
            <CardDescription>Error</CardDescription>
            <CardTitle className="text-destructive">{error}</CardTitle>
          </CardHeader>
        </Card>
      </div>
    );
  }

  if (!stats) {
    return null;
  }

  return (
    <div className="grid grid-cols-1 gap-4 *:data-[slot=card]:shadow-xs lg:grid-cols-4">
      {/* Card 1 - Volumen Total */}
      <Card className="@container/card">
        <CardHeader>
          <CardDescription>Volumen Total</CardDescription>
          <CardTitle className="text-2xl font-semibold tabular-nums @[250px]/card:text-3xl">{formatCurrency(stats.volumen)}</CardTitle>
        </CardHeader>
        <CardFooter className="flex-col items-start gap-1.5 text-sm">
          <div className="line-clamp-1 flex gap-2 font-medium">Total transaccionado</div>
          <div className="text-muted-foreground">Suma de todos los montos</div>
        </CardFooter>
      </Card>

      {/* Card 2 - Transacciones Control */}
      <Card className="@container/card">
        <CardHeader>
          <CardDescription>Transacciones Control</CardDescription>
          <CardTitle className="text-2xl font-semibold tabular-nums @[250px]/card:text-3xl">{formatNumber(stats.control)}</CardTitle>
        </CardHeader>
        <CardFooter className="flex-col items-start gap-1.5 text-sm">
          <div className="line-clamp-1 flex gap-2 font-medium">Transacciones sobre umbral</div>
          <div className="text-muted-foreground">Montos que superan el límite</div>
        </CardFooter>
      </Card>

      {/* Card 3 - Comercio Top */}
      <Card className="@container/card">
        <CardHeader>
          <CardDescription>Comercio Top</CardDescription>
          <CardTitle className="text-2xl font-semibold tabular-nums @[250px]/card:text-3xl">{formatNumber(stats.habito.transactionCount)}</CardTitle>
        </CardHeader>
        <CardFooter className="flex-col items-start gap-1.5 text-sm">
          <div className="line-clamp-1 flex gap-2 font-medium">{stats.habito.businessName}</div>
          <div className="text-muted-foreground">Comercio con más transacciones</div>
        </CardFooter>
      </Card>

      {/* Card 4 - Ticket Promedio */}
      <Card className="@container/card">
        <CardHeader>
          <CardDescription>Ticket Promedio</CardDescription>
          <CardTitle className="text-2xl font-semibold tabular-nums @[250px]/card:text-3xl">{formatCurrency(stats.foco)}</CardTitle>
        </CardHeader>
        <CardFooter className="flex-col items-start gap-1.5 text-sm">
          <div className="line-clamp-1 flex gap-2 font-medium">Promedio por transacción</div>
          <div className="text-muted-foreground">Monto promedio de transacciones</div>
        </CardFooter>
      </Card>
    </div>
  );
}
