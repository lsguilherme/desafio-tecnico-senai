import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import './App.css'
import Header from './components/layout/Header'
import Sidebar from './components/layout/Sidebar'
import ProductList from './pages/ProductList'
import ProductForm from './pages/ProductForm'
import NotFound from './pages/NotFound'


function App() {
  return (
    <Router>
      <div className="flex min-h-screen bg-gray-100">
        <Sidebar />
        <div className="flex-1 flex flex-col">
          <Header />
          <Routes>
            <Route path="/" element={<Navigate to="/products" replace />} />
            <Route path="/products" element={<ProductList />} />
            <Route path="/products/new" element={<ProductForm />} />
            <Route path="/products/edit/:id" element={<ProductForm />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </div>
      </div>
    </Router>
  )
}

export default App
