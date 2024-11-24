import React, {PropsWithChildren, ReactNode, useEffect, useLayoutEffect, useRef, useState} from "react";
import ReactDOM, {createPortal} from "react-dom";
import './sui-modal.scss'

function createWrapperAndAppendToBody(wrapperId: string) {
  const wrapperElement = document.createElement('div');
  wrapperElement.setAttribute("id", wrapperId);
  document.body.appendChild(wrapperElement);
  return wrapperElement;
}


function ReactPortal({children, wrapperId = "react-portal-wrapper"}: PropsWithChildren<{
  readonly wrapperId: string
}>) {
  const [wrapperElement, setWrapperElement] = useState<HTMLElement | null>(null);

  useLayoutEffect(() => {
    let element = document.getElementById(wrapperId);
    let systemCreated = false;
    // if element is not found with wrapperId or wrapperId is not provided,
    // create and append to body
    if (!element) {
      systemCreated = true;
      element = createWrapperAndAppendToBody(wrapperId);
    }
    setWrapperElement(element);

    return () => {
      // delete the programatically created element
      if (systemCreated && element.parentNode) {
        element.parentNode.removeChild(element);
      }
    }
  }, [wrapperId]);

  // wrapperElement state will be null on very first render.
  if (wrapperElement === null) return null;

  return createPortal(children, wrapperElement);
}

export function SuiModal({children, isOpen, handleClose}: PropsWithChildren<{
  readonly isOpen: boolean
  readonly handleClose: () => void
}>) {
  const nodeRef = useRef(null);
  useEffect(() => {
    const closeOnEscapeKey = (e: { key: string; }) => (e.key === "Escape" ? handleClose() : null);
    document.body.addEventListener("keydown", closeOnEscapeKey);
    return () => {
      document.body.removeEventListener("keydown", closeOnEscapeKey);
    };
  }, [handleClose]);

  return (
    <ReactPortal wrapperId="react-portal-modal-container">
      {isOpen && <div className="modal" ref={nodeRef}>
        <button onClick={handleClose} className="close-btn">
          Close
        </button>
        <div className="modal-content">{children}</div>
      </div>}
    </ReactPortal>
  );
}

