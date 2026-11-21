import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./design-system/tokens.css";
import { App } from "./app/App";
import { RpcProvider } from "./rpc/transport";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <RpcProvider>
      <App />
    </RpcProvider>
  </StrictMode>,
);
