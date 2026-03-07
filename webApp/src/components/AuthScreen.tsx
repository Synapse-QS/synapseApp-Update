import React from 'react';
import { useNavigate } from 'react-router-dom';
import './AuthScreen.css';

const SynapseLogo = () => (
  <svg width="60" height="60" viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
    <circle cx="50" cy="50" r="45" stroke="var(--primary-indigo)" strokeWidth="2" strokeDasharray="10 5" />
    <path d="M50 20 L80 50 L50 80 L20 50 Z" stroke="var(--primary-violet)" strokeWidth="3" fill="rgba(139, 92, 246, 0.2)" />
    <circle cx="50" cy="20" r="5" fill="var(--primary-indigo)" />
    <circle cx="80" cy="50" r="5" fill="var(--primary-indigo)" />
    <circle cx="50" cy="80" r="5" fill="var(--primary-indigo)" />
    <circle cx="20" cy="50" r="5" fill="var(--primary-indigo)" />
  </svg>
);

const AuthScreen: React.FC = () => {
  const navigate = useNavigate();

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    // Simulate login success
    navigate('/feed');
  };

  return (
    <div className="auth-container">
      <div className="auth-background-effects">
        <div className="glow-sphere indigo"></div>
        <div className="glow-sphere violet"></div>
      </div>
      
      <div className="glass-card login-card animate-fade-in">
        <div className="auth-header">
          <SynapseLogo />
          <h1>Enter Synapse</h1>
          <p>Next-Gen Social Experience</p>
        </div>
        
        <form className="auth-form" onSubmit={handleLogin}>
          <div className="input-group">
            <label htmlFor="email">Email Address</label>
            <input type="email" id="email" placeholder="name@synapse.social" required />
          </div>
          
          <div className="input-group">
            <label htmlFor="password">Password</label>
            <input type="password" id="password" placeholder="••••••••" required />
          </div>
          
          <div className="auth-footer-links">
            <a href="#">Forgot Password?</a>
          </div>
          
          <button type="submit" className="primary btn-block glow-indigo">Enter Synapse</button>
        </form>
        
        <div className="auth-switch">
          <span>New to Synapse?</span>
          <a href="#">Create an Account</a>
        </div>
      </div>
    </div>
  );
};

export default AuthScreen;
