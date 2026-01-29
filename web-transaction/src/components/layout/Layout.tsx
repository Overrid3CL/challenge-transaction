import { useState } from 'react'
import { Outlet } from 'react-router-dom'
import { Navbar } from './Navbar'
import { MobileSidebar } from './MobileSidebar'
import { Toaster } from 'sonner'

export function Layout() {
  const [sidebarOpen, setSidebarOpen] = useState(false)

  return (
    <div className="flex min-h-screen flex-col">
       <Toaster richColors position="top-center" />
      <Navbar onMenuClick={() => setSidebarOpen(true)} />
      <MobileSidebar open={sidebarOpen} onOpenChange={setSidebarOpen} />
      <main className="flex-1">
        <Outlet />
      </main>
    </div>
  )
}
