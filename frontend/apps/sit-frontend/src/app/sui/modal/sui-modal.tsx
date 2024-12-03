import React, {PropsWithChildren, useEffect} from "react";
import ReactDOM from "react-dom";
import './sui-modal.scss'
import classNames from "classnames";

export const Modal = ({isOpen, onClose, children, centered}: PropsWithChildren<{
  readonly isOpen: boolean
  readonly onClose: () => void
  readonly centered?: boolean
}>) => {
  // Close the modal when the Escape key is pressed
  useEffect(() => {
    const handleEscape = (event: { key: string; }) => {
      if (event.key === 'Escape') {
        onClose();
      }
    };
    document.addEventListener('keydown', handleEscape);
    return () => document.removeEventListener('keydown', handleEscape);
  }, [onClose]);

  // Render nothing if modal is not open
  if (!isOpen) return null;

  // Create portal to render modal in a dedicated DOM node
  return ReactDOM.createPortal(
    <div className={classNames("modal-backdrop", { "modal-backdrop-centered": centered })} onClick={onClose}>
      <div className={classNames("modal-content", { "modal-content-centered": centered })} onClick={(e) => e.stopPropagation()}>
        <button className="modal-close" onClick={onClose}>
          &times;
        </button>
        {children}
      </div>
    </div>,
    document.getElementById('modal-root') as HTMLElement // Make sure this exists in your HTML
  );
};

