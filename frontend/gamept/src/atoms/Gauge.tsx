import { IGauge } from "@/types/components/Gauge.types";
import GaugeImg from "@/assets/gauge_bg.png";

const Gauge = (props: IGauge) => {
  return (
    <div className="w-full h-[45px] flex justify-center -mb-3">
      <img className="w-auto h-full" src={GaugeImg} alt={`gauge_${props.type}`} />
    </div>
  );
}

export default Gauge;