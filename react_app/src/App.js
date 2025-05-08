import React from 'react';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import routes from './routes';
import './App.css';
import NotFoundPage from './pages/NotFound';
import HomePage from './pages/HomePage';
import FeedPage from './pages/FeedPage';

function App() {
  return (
    <Router>
        <div className='App'>
            <Routes>
                {/* 
                <Route path={urls[0].value} element={<GroupsTimetable />} />
                <Route path={urls[1].value} element={<TeachersTimetable />} />
                <Route path={urls[2].value} element={<AudiencesTimetable />} />
                <Route path={urls[3].value} element={<SubjectsTimetable />} />
                */}
                <Route path={routes.FEED_PAGE} element={<FeedPage />} /> {/* Feed */}
                <Route path={routes.HOME_PAGE} element={<HomePage />} /> {/* Home */}
                <Route path="*" element={<NotFoundPage />} /> {/* Fallback */}
            </Routes>
        </div>
    </Router>
  );
}

export default App;
