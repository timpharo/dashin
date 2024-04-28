import type { Component } from 'solid-js';
import {Countdown} from "../types/Countdown";

const CountdownItem: Component = (countdown: Countdown) => {
  return (
      <div className="m-2 float-start">
        <div className="card bg-neutral text-neutral-content card-compact">
            <div className="card-body">
                <h2 className="card-title">
                    ⏲️ Countdown
                </h2>
                <div className="stat">
                    <div className="stat-desc">
                        { countdown.description }
                    </div>
                    <div className="stat-value">
                        <span>{ countdown.daysUntil }d </span>
                        <span>{ countdown.hoursUntil }h </span>
                        <span>{ countdown.minutesUntil }m </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
  );
};

export default CountdownItem;
