import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.tsx';
import './index.css';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import CreateGamePage from './pages/CreateGamePage.tsx';
// 라우팅 경로 지정
const router = createBrowserRouter([
  { path: '/', element: <App /> },
  { path: '/createGame', element: <CreateGamePage /> },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
