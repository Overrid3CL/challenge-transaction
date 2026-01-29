import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogMedia,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog"
import { Trash2Icon } from "lucide-react"

interface Props{
    isOpen: boolean;
    onConfirm: () => void;
    onCancel: () => void;
    isLoading: boolean;
    title: string;
    description: string;
}

export function ConfirmDeleteDialog({ 
    isOpen, 
    onConfirm, 
    onCancel, 
    isLoading = false, 
    title = "¿Estás completamente seguro?",
    description = "Esta acción no se puede deshacer. Esto eliminará permanentemente el registro.",
 }: Props) {
    return (
<AlertDialog open={isOpen} onOpenChange={onCancel}>

  <AlertDialogContent  size="sm">
    <AlertDialogHeader>
         <AlertDialogMedia className="bg-destructive/10 text-destructive dark:bg-destructive/20 dark:text-destructive">
            <Trash2Icon />
          </AlertDialogMedia>
      <AlertDialogTitle>{title}</AlertDialogTitle>
      <AlertDialogDescription>{description}
      </AlertDialogDescription>
    </AlertDialogHeader>
    <AlertDialogFooter>
      <AlertDialogCancel  variant="outline" disabled={isLoading}>Cancelar</AlertDialogCancel>
      <AlertDialogAction  variant="destructive"
            onClick={(e) => {
              e.preventDefault(); // Evita que se cierre automáticamente si hay un error
              onConfirm();
            }}
            className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
            disabled={isLoading}
          >
            {isLoading ? "Eliminando..." : "Eliminar"}
          </AlertDialogAction>
    </AlertDialogFooter>
  </AlertDialogContent>
</AlertDialog>
    )
}
