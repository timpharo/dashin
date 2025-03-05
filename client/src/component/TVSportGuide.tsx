

import type { Component } from 'solid-js';

const TVSportGuide: Component = () => {
    return (
        <div className="m-2 float-start">
            <div className="card bg-neutral text-neutral-content card-compact">
                <div className="card-body">
                    <h2 className="card-title">
                        Sport on TV (3 days)
                    </h2>
                    {/*<script>var eventMethod = window.addEventListener ? "addEventListener" : "attachEvent"; var eventer*/}
                    {/*    =*/}
                    {/*    window[eventMethod]; var messageEvent = eventMethod == "attachEvent" ? "onmessage" : "message";*/}
                    {/*    eventer(messageEvent,function(e) {{*/}
                    {/*        if(*/}
                    {/*        typeof e.data == "number" && e.data > 100) document.getElementById("outputFrame").height = e.data + "px";}},false);*/}
                    {/*</script>*/}
                    <iframe className="frameData" id="outputFrame"
                            src="https://sport-tv-guide.live/sportwidget/ed8dac5c0a64?time_zone=Europe%2FLondon&fc=2&time12=0&sports=18,1,32,7,39,40,12&bg=f8f8f9&bgs=b7b7b7&grp=1&sd=0&lng=1"
                            style="position:relative;border:none;height:40em;width: 35em;" frameBorder="0"></iframe>
                    <div style="padding:5px;text-align:center;font-size:10px">Powered by <a
                        href="https://sport-tv-guide.live">Live
                        Sports TV Guide</a></div>
                </div>
            </div>
        </div>


    );
};

export default TVSportGuide;

