
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import UserService from '../service/UsersService';
import {storage} from "../../firebase";
import {ref,uploadBytesResumable,getDownloadURL} from 'firebase/storage'

function UpdateUser() {
    const navigate = useNavigate();
    const { userId } = useParams();
    const [progress,setProgress] = useState(null)
    console.log(progress)
    const [file,setFile]= useState(null)


    const [userData, setUserData] = useState({
        name: '',
        email: '',
        role: '',
        city: '',
        img: ''
    });
    console.log(userData)
    useEffect(() => {
        fetchUserDataById(userId); // Pass the userId to fetchUserDataById
    }, [userId]); //wheen ever there is a chane in userId, run this

    const fetchUserDataById = async (userId) => {
        try {
            const token = localStorage.getItem('token');
            const response = await UserService.getUserById(userId, token);
            // Pass userId to getUserById
            const { name, email, role, city,img } = response.ourUsers;
            setUserData({ name, email, role, city, img });
        } catch (error) {
            console.error('Error fetching user data:', error);
        }
    };
    useEffect(()=>{
            const UploadFile = () =>{
                const storageRef = ref(storage,file.name);
                const uploadTask = uploadBytesResumable(storageRef,file);
                uploadTask.on('state_changed',(snapshot) =>{
                    const progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                    setProgress(progress)
                    switch (snapshot.state) {
                        case  'paused':
                            console.log("Upload is paused")
                            break;
                        case 'running':
                            console.log("Upload is running")
                            break;
                        default:
                            console.log("Upload is unknown")
                            break
                    }
                },(error) => {
                    console.log(error)
                },() => {
                    getDownloadURL(uploadTask.snapshot.ref).then((downloadURL) => {
                        setUserData((prevUserData) => ({
                           ...prevUserData,
                            img: downloadURL
                        }))
                    })
                })
            }
            file && UploadFile();
    },[file])

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUserData((prevUserData) => ({
            ...prevUserData,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const confirmDelete = window.confirm('Are you sure you want to delete this user?');
            if (confirmDelete) {
                const token = localStorage.getItem('token');
                const res = await UserService.updateUser(userId, userData, token);
                console.log(res)
                // Redirect to profile page or display a success message
                navigate("/admin/user-management")
            }

        } catch (error) {
            console.error('Error updating user profile:', error);
            alert(error)
        }
    };

    return (
        <div className="auth-container">
            <h2>Update User</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Name:</label>
                    <input type="text" name="name" value={userData.name} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" value={userData.email} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                    <label>Role:</label>
                    <input type="text" name="role" value={userData.role} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                    <label>City:</label>
                    <input type="text" name="city" value={userData.city} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                    <label>Image:</label>
                    <div>
                        <img src={userData.img} style={{height: '100px', width: '100px',borderRadius: "50%",objectFit : 'cover'}}/>
                        <input type='file' onChange={(e) => setFile(e.target.files[0])}/>
                    </div>
                </div>
                <button type="submit" disabled={progress !== null && progress < 100}>Update</button>
            </form>
        </div>
    );
}

export default UpdateUser;
