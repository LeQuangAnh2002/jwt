
import React, {useEffect, useState} from 'react';
import UserService from '../service/UsersService';
import { useNavigate } from 'react-router-dom';
import {ErrorMessage, Field, Form, Formik, useFormik, useFormikContext} from "formik";
import *  as Yup from "yup";
import {storage} from "../../firebase";
import {ref,uploadBytesResumable,getDownloadURL} from 'firebase/storage'


function RegistrationPage() {
    const navigate = useNavigate();
    const [url,setUrl]= useState({})
    const [progress,setProgress] = useState(null)
    const [file,setFile]= useState(null)
    console.log("progress",progress)
    const initialValues = {
        name: '',
        email: '',
        password: '',
        role: '',
        city: ''
    }
        const validateSchema = {
            name: Yup.string()
                .required("Name is not empty"),
            email: Yup.string()
                .required("Email is not empty"),
            password: Yup.string()
                .required("Password is not empty"),
            role: Yup.string()
                .required("Role is not empty"),
            city: Yup.string()
                .required("City is not empty")
        }
        const RegisterUser = async (values,formikProps) => {
            const updateValues = {
                ...values,
                img: url.img
            }
            try {
                // Call the register method from UserService
                const token = localStorage.getItem('token');
                await UserService.register(updateValues, token);
                navigate('/admin/user-management');

            } catch (error) {
                if (error.response && error.response.data) {
                    Object.keys(error.response.data).forEach((field) => {
                        formikProps.setFieldError(field, error.response.data[field]);
                    });
                }
                console.log(error)
            }
        };
    useEffect(() => {
        const uploadFile = () => {
            const storageRef = ref(storage,file.name);
            const uploadTask = uploadBytesResumable(storageRef,file);
            uploadTask.on('state_changed',(snapshot) => {
                // Tính toán phần trăm tiến độ upload
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
                        setUrl((prev) =>({...prev,img:downloadURL}))

                })
            })
        }
        file && uploadFile()
    },[file])
        return (

            <div className="auth-container">
                <h2>Registration</h2>
                <Formik initialValues={initialValues} onSubmit={async (value,formikProps) => await RegisterUser(value,formikProps)}
                        validationSchema={Yup.object(validateSchema)}>
                    <Form>
                        <div className="form-group">
                            <label htmlFor="name">Name:</label>
                            <Field type="text" name="name" id="name"/>
                            <ErrorMessage name="name" component="span" style={{color: "red"}}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="email">Email:</label>
                            <Field type="email" name="email" id="email"/>
                            <ErrorMessage name="email" component="span" style={{color: "red"}}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="password">Password:</label>
                            <Field type="password" name="password" id="password"/>
                            <ErrorMessage name="password" component="span" style={{color: "red"}}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="role">Role:</label>
                            <Field type="text" name="role" id="role"/>
                            <ErrorMessage name="role" component="span" style={{color: "red"}}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="city">City:</label>
                            <Field type="text" name="city" id="city"/>
                            <ErrorMessage name="city" component="span" style={{color: "red"}}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="city">Upload:</label>
                            <Field type="file" name="img" id="img" onChange={(e) => setFile(e.target.files[0])}/>
                            <ErrorMessage name="img" component="span" style={{color: "red"}}/>
                        </div>
                        <div className="form-group">
                            <button type="submit" disabled={progress !== null && progress < 100}>Register</button>
                        </div>
                    </Form>
                </Formik>

            </div>
        );

}
export default RegistrationPage;


// const [formData, setFormData] = useState({
//     name: '',
//     email: '',
//     password: '',
//     role: '',
//     city: ''
// });


// const handleInputChange = (e) => {
//     const { name, value } = e.target;
//     setFormData({ ...formData, [name]: value });
// };

// const handleSubmit = async (e) => {
//     e.preventDefault();
//     try {
//         // Call the register method from UserService
//
//         const token = localStorage.getItem('token');
//         await UserService.register(formData, token);
//
//         // Clear the form fields after successful registration
//         setFormData({
//             name: '',
//             email: '',
//             password: '',
//             role: '',
//             city: ''
//         });
//         alert('User registered successfully');
//         navigate('/admin/user-management');
//
//     } catch (error) {
//         setErrors(error.response.data)
//         console.error('Error registering user:', error);
//
//     }
// };

    {/*{Object.keys(errors).map((key) => (*/}
    {/*    <div key={key}>*/}
    {/*        <strong>{key}:</strong>*/}
    {/*        <ul>*/}
    {/*            { <li>{errors[key]}</li>}*/}
    {/*        </ul>*/}
    {/*    </div>*/}
    {/*))}*/}

    {/*<form onSubmit={handleSubmit}>*/}
    {/*    <div className="form-group">*/}
    {/*        <label>Name:</label>*/}
    {/*        <input type="text" name="name" value={formData.name} onChange={handleInputChange}  />*/}
    {/*    </div>*/}
    {/*    <div className="form-group">*/}
    {/*        <label>Email:</label>*/}
    {/*        <input type="email" name="email" value={formData.email} onChange={handleInputChange}  />*/}
    {/*    </div>*/}
    {/*    <div className="form-group">*/}
    {/*        <label>Password:</label>*/}
    {/*        <input type="password" name="password" value={formData.password} onChange={handleInputChange}  />*/}
    {/*    </div>*/}
    {/*    <div className="form-group">*/}
    {/*        <label>Role:</label>*/}
    {/*        <input type="text" name="role" value={formData.role} onChange={handleInputChange} placeholder="Enter your role"  />*/}
    {/*    </div>*/}
    {/*    <div className="form-group">*/}
    {/*        <label>City:</label>*/}
    {/*        <input type="text" name="city" value={formData.city} onChange={handleInputChange} placeholder="Enter your city"  />*/}
    {/*    </div>*/}
    {/*    <button type="submit">Register</button>*/}
    {/*</form>*/}


