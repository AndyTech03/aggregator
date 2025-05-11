import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Cookies from 'js-cookie';
import assert from './utils/assert';

import routes from './routes';
import './App.css';

import { UserContext } from './contexts/UserContext';
import NotFoundPage from './pages/NotFound';

import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import useValue from './hooks/useValue';
import ArticlePage from './pages/ArticlePage';
import SearchPage from './pages/SearchPage';
import FeedPage from './pages/FeedPage';


function App() {
  const [userId, setUserId] = useValue({
    cookiesName: 'userId', 
    global: true,
  })
  const [profile, setProfile] = useState('all')

  const logInUser = (login, password) => {
    const value = 'test-' + login + '-' + password
    setUserId(value)
  }

  return (
    <Router>
      <UserContext.Provider value={{userId, profile, setProfile}}>
        <div className='App'>
          <Routes>
              <Route path={routes.LOGIN_PAGE} element={<LoginPage logInUser={logInUser} />} /> {/* Login */}
              <Route path={routes.SEARCH_PAGE} element={<SearchPage />} /> {/* Search */}
              <Route path={routes.FEED_PAGE} element={<FeedPage />} /> {/* Feed */}
              <Route path={routes.ARTICLE_PAGE} element={<ArticlePage />} /> {/* Article */}
              <Route path={routes.HOME_PAGE} element={<HomePage />} /> {/* Home */}
              <Route path="*" element={<NotFoundPage />} /> {/* Fallback */}
          </Routes>
        </div>
      </UserContext.Provider>
    </Router>
  );
}

export default App;
