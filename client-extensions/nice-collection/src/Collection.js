import React from 'react';

function Collection(props) {
  const items = [];

  for (let i = 0; i < props.numberOfItems; i++) {
    const slotName = "item-" + i;
    items.push(<slot key={slotName} name={slotName} />);
  }

  return (
    <div>
        <style>
            {props.parentStyles}
        </style>
        <div className="container">
            <div className="row">
                <div className="col-sm-8">
                    {items[0]}
                </div>
                <div className="col-sm-4">
                    {items[1] && <div>{items[1]}</div>}
                    {items[2] && <div>{items[2]}</div>}
                </div>
            </div>
            {props.numberOfItems > 3 && (
                <div className="row">
                    {items.slice(3).map((item, index) => (
                        <div key={index + 3} className="col-sm-4">
                            {item}
                        </div>
                    ))}
                </div>
            )}
        </div>
    </div>
  );
}

export default Collection;