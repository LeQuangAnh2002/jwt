
import React, { useState, useEffect } from 'react';
import UserService from '../service/UsersService';
import { Link } from 'react-router-dom';
import LoadingIcon from "../common/Loading";


function ProfilePage() {
    const [profileInfo, setProfileInfo] = useState({});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchProfileInfo();
        setLoading(false);
    }, []);

    const fetchProfileInfo = async () => {
        try {

            const token = localStorage.getItem('token'); // Retrieve the token from localStorage
            const response = await UserService.getYourProfile(token);

            setProfileInfo(response.ourUsers);
        } catch (error) {
            console.error('Error fetching profile information:', error);
        }
    };

    return loading ?(<LoadingIcon/>) : (
        <div className="profile-page-container">
            <h2>Profile Information</h2>
            <p>Name: {profileInfo.name}</p>
            <p>Email: {profileInfo.email}</p>
            <p>City: {profileInfo.city}</p>
            {profileInfo.role === "ADMIN" && (
                <button><Link to={`/update-user/${profileInfo.id}`}>Update This Profile</Link></button>
            )}
        </div>
    );
}

export default ProfilePage;
