import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import AuthPage from './pages/AuthPage';
import HomePage from './pages/HomePage';
import CreateRecipePage from './pages/CreateRecipePage';
import EditRecipePage from "./pages/EditRecipePage";
import CookRecipePage from "./pages/CookRecipePage";


function App() {
  return (
      <Router>
        <Routes>
          <Route path="/auth" element={<AuthPage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/" element={<AuthPage />} />
            <Route path="/create-recipe" element={<CreateRecipePage />} /> {/* ← новый маршрут */}
            <Route path="/edit-recipe/:id" element={<EditRecipePage />} /> {/* ← НОВЫЙ */}
            <Route path="/cook-recipe/:id" element={<CookRecipePage />} />
        </Routes>
      </Router>
  );
}

export default App;