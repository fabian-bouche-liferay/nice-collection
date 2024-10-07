import React, { useEffect, useRef } from 'react';

function ThreeDSlider(props) {
  const items = [];
  const delta = 360 / props.numberOfItems;
  const itemRefs = useRef([]);

  useEffect(() => {
    itemRefs.current.forEach((item) => {
        if (item) {
            const contentHeight = item.firstChild.assignedNodes()[0].offsetHeight;
            const containerHeight = item.offsetHeight;

            const scale = containerHeight / contentHeight;
            item.firstChild.assignedNodes()[0].style.transform = `scale(${scale})`;

        }
    });
  }, [props.numberOfItems]);

  for (let i = 0; i < props.numberOfItems; i++) {
    const slotName = "item-" + i;
    items.push(<slot key={slotName} name={slotName} />);
  }

  return (
    <div className="threeDSliderContainer">
        <style>
            {props.parentStyles}
        </style>
        
        <div className="threeDSlider">

            {items.map((item, index) => (
                <div
                    ref={(el) => itemRefs.current[index] = el} 
                    key={index}
                    style={{ '--angle': ((index + 1) * delta) }}
                    className="sliderElement"
                    >
                    {item}
                </div>
            ))}

        </div>
    </div>
  );
}

export default ThreeDSlider;