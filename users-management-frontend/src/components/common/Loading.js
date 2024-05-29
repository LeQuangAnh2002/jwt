import  loading from '../../assets/images/Spin@1x-1.0s-200px-200px.gif';
import React from "react";

const LoadingIcon = () => <img src={loading} alt='Loading...' style={{width: "50px",
    height: "50px",
    borderRadius: "50%",
    objectFit: "cover"}}/>
export default LoadingIcon;