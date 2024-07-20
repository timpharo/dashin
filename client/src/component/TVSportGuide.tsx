

import type { Component } from 'solid-js';

const TVSportGuide: Component = () => {
    return (
        <div className="m-2 float-start">
            <div className="card bg-neutral text-neutral-content card-compact">
                <div className="card-body">
                    <h2 className="card-title">
                        Sport on TV (3 days)
                    </h2>
            <iframe
                src="https://www.tvsportguide.com/widget/669be0d4899bf?heading=Events&border_color=custom&autoscroll=1&custom_colors=a6adbb,2a323c,ffffff"
                    frameBorder="0" style="width: 300px; height: 400px; border: none"></iframe>
        </div>
            </div>
        </div>

    );
};

export default TVSportGuide;

