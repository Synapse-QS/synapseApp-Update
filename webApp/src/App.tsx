import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import AuthScreen from './components/AuthScreen'
import FeedScreen from './components/FeedScreen'
import ChatScreen from './components/ChatScreen'
import ProfileScreen from './components/ProfileScreen'
import './App.css'

function App() {
  return (
    <Router>
      <div className="app-root w-full h-full bg-background-dark text-slate-100 selection:bg-primary/30">
        <Routes>
          {/* Default to Auth for now, or redirect based on logic later */}
          <Route path="/" element={<Navigate to="/auth" replace />} />
          
          <Route path="/auth" element={<AuthScreen />} />
          <Route path="/feed" element={<FeedScreen />} />
          <Route path="/chat" element={<ChatScreen />} />
          <Route path="/profile" element={<ProfileScreen />} />
          
          {/* Catch-all for 404 behavior or redirecting back */}
          <Route path="*" element={<Navigate to="/auth" replace />} />
        </Routes>
      </div>
    </Router>
  )
}

export default App
