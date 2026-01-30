import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Layout } from "./components/layout/Layout";
import { HomePage } from "./pages/HomePage";
import { TransactionsPage } from "./pages/TransactionsPage";
import { PerfilPage } from "./pages/PerfilPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<HomePage />} />
          <Route path="transactions" element={<TransactionsPage />} />
          <Route path="perfil" element={<PerfilPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
