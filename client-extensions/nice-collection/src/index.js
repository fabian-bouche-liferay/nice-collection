import React from 'react';
import ReactDOM from 'react-dom';
import Collection from './Collection';
import ThreeDSlider from './ThreeDSlider';
import "./ThreeDSlider.css";

class NiceCollectionWebComponent extends HTMLElement {

    constructor() {
        super();
        if (!this.shadowRoot) {
            this.attachShadow({ mode: 'open' });
        }
    }

    connectedCallback() {

        this.renderReactComponent();

    }

    renderReactComponent() {
        const parentStyles = Array.from(document.styleSheets)
            .map((sheet) => {
                try {
                    return Array.from(sheet.cssRules)
                        .map((rule) => rule.cssText)
                        .join('');
                } catch (e) {
                    return '';
                }
            })
            .join('');

        this.shadowRoot.innerHTML = `
            <!-- Slots placeholder -->
        `;

        const rows = this.querySelectorAll('.slot-item');
        
        let numberOfItems = rows.length;

        ReactDOM.render(
            <Collection parentStyles={parentStyles} numberOfItems={numberOfItems} />,
            this.shadowRoot
        );
    }

    disconnectedCallback() {
        ReactDOM.unmountComponentAtNode(this.shadowRoot);
    }
}

class ThreeDSliderWebComponent extends HTMLElement {

    constructor() {
        super();
        if (!this.shadowRoot) {
            this.attachShadow({ mode: 'open' });
        }
    }

    connectedCallback() {

        this.renderReactComponent();

    }

    renderReactComponent() {
        const parentStyles = Array.from(document.styleSheets)
            .map((sheet) => {
                try {
                    return Array.from(sheet.cssRules)
                        .map((rule) => rule.cssText)
                        .join('');
                } catch (e) {
                    return '';
                }
            })
            .join('');

        this.shadowRoot.innerHTML = `
            <!-- Slots placeholder -->
        `;

        const rows = this.querySelectorAll('.slot-item');
        
        let numberOfItems = rows.length;

        ReactDOM.render(
            <ThreeDSlider parentStyles={parentStyles} numberOfItems={numberOfItems} />,
            this.shadowRoot
        );
    }

    disconnectedCallback() {
        ReactDOM.unmountComponentAtNode(this.shadowRoot);
    }
}

const NICE_COLLECTION_ELEMENT_ID = 'nice-collection';

if (!customElements.get(NICE_COLLECTION_ELEMENT_ID)) {
	customElements.define(NICE_COLLECTION_ELEMENT_ID, NiceCollectionWebComponent);
}

const THREE_D_SLIDER_ELEMENT_ID = 'three-d-slider';

if (!customElements.get(THREE_D_SLIDER_ELEMENT_ID)) {
	customElements.define(THREE_D_SLIDER_ELEMENT_ID, ThreeDSliderWebComponent);
}