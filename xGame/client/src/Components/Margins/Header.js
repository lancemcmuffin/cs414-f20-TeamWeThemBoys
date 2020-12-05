import React, { useState } from "react";
import PropTypes from "prop-types";
import "./MarginStyle.css";
import {
  Button,
  AppBar,
  Toolbar,
  IconButton,
  Typography,
  makeStyles,
  Drawer,
  Card,
  CardContent,
  CardActions
} from "@material-ui/core";
import NotificationsIcon from "@material-ui/icons/Notifications";
import AccountCircle from "@material-ui/icons/AccountCircle";
import ChevronLeftIcon from "@material-ui/icons/ChevronLeft";
import ChevronRightIcon from "@material-ui/icons/ChevronRight";

import Profile from "../Profile/Profile";
import UnregisterDialog from "./UnregisterDialog";
import * as matchService from "../../service/matchService";
import * as userService from "../../service/userService";

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1
  },
  logout: {
    color: "#ffffff",
    textTransform: "inherit"
  },
  drawerPaper: {
    width: 240
  },
  notificationsDrawerHeader: {
    display: "flex",
    alignItems: "center",
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
    justifyContent: "flex-end"
  },
  profileDrawerHeader: {
    display: "flex",
    alignItems: "center",
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
    justifyContent: "flex-start"
  },
  notificationTitle: {
    fontSize: 14
  }
}));

const Header = (props) => {
  const { activeUser, logOutUser, setActiveMatch, fetchNotifications } = props;

  const [showNotifications, setShowNotifications] = useState(false);
  const [showProfile, setShowProfile] = useState(false);
  const [profileData, setProfileData] = useState({});
  const [
    showUnregisterConfirmation,
    setShowUnregisterConfirmation
  ] = React.useState(false);

  const toggleNotifications = async () => {
    if (!showNotifications) await fetchNotifications();
    setShowNotifications(!showNotifications);
  };

  const toggleShowProfile = async () => {
    if (!showProfile) {
      const newProfileData = await userService.getProfile(activeUser?.id);
      if (newProfileData) setProfileData(newProfileData);
    }
    setShowProfile(!showProfile);
  };

  const handleAcceptMatch = async (matchId) => {
    const matchData = await matchService.acceptInvite(matchId);
    if (matchData) setActiveMatch(matchData);
    toggleNotifications();
  };

  const handleRejectMatch = (matchId) => {
    console.log("TODO: Implement match rejection");
  };

  const classes = useStyles();

  const Notifications = () => {
    if (activeUser?.notifications) {
      return (
        <div>
          {activeUser.notifications.map((notification) => {
            const isInvitation = notification?.type === "INVITATION";
            return (
              <Card key={notification?.id}>
                <CardContent>
                  <Typography
                    className={classes.notificationTitle}
                    color="textSecondary"
                    align="left"
                    gutterBottom
                  >
                    {isInvitation ? "Invitation" : "Message"}
                  </Typography>
                  <Typography variant="h6" component="h2" align="left">
                    {notification?.content}
                    {isInvitation && " is inviting you to a match."}
                  </Typography>
                  {isInvitation && (
                    <CardActions>
                      <Button
                        size="small"
                        color="primary"
                        variant="contained"
                        onClick={() => handleAcceptMatch(notification?.id)}
                      >
                        Accept
                      </Button>
                      <Button
                        size="small"
                        color="secondary"
                        variant="contained"
                        onClick={() => handleRejectMatch(notification?.id)}
                      >
                        Reject
                      </Button>
                    </CardActions>
                  )}
                </CardContent>
              </Card>
            );
          })}
        </div>
      );
    }
    return <div>No Notifications!</div>;
  };

  return (
    <div className={"header"}>
      {activeUser.isLoggedIn ? (
        <>
          <AppBar position="static">
            <Toolbar>
              <IconButton color="inherit" onClick={toggleNotifications}>
                <NotificationsIcon />
              </IconButton>
              <div className={classes.grow}></div>
              <Typography variant="h6">Legan Chess Online</Typography>
              <div className={classes.grow}></div>
              <Button onClick={logOutUser} className={classes.logout}>
                Log out
              </Button>
              <IconButton color="inherit" onClick={toggleShowProfile}>
                <AccountCircle />
              </IconButton>
            </Toolbar>
          </AppBar>
          <Drawer
            variant="persistent"
            anchor="left"
            open={showNotifications}
            classes={{ paper: classes.drawerPaper }}
          >
            <div className={classes.notificationsDrawerHeader}>
              <Typography variant="h6">Notifications</Typography>
              <IconButton onClick={toggleNotifications}>
                <ChevronLeftIcon />
              </IconButton>
            </div>
            <Notifications />
          </Drawer>
          <Drawer
            variant="persistent"
            anchor="right"
            open={showProfile}
            classes={{ paper: classes.drawerPaper }}
          >
            <div className={classes.profileDrawerHeader}>
              <IconButton onClick={() => setShowProfile(false)}>
                <ChevronRightIcon />
              </IconButton>
              <Typography variant="h6" className="pr-2">
                Profile
              </Typography>
            </div>
            <Button variant="contained" onClick={logOutUser}>
              Log out
            </Button>
            <Button
              color="secondary"
              onClick={() => {
                setShowUnregisterConfirmation(true);
              }}
              size="small"
            >
              Deregister
            </Button>
            <Profile profileData={profileData} />
          </Drawer>
          <UnregisterDialog
            showUnregisterConfirmation={showUnregisterConfirmation}
            setShowUnregisterConfirmation={setShowUnregisterConfirmation}
            activeUser={activeUser}
            logoutUser={logOutUser}
          />
        </>
      ) : (
        <h3>Please Login or Register</h3>
      )}
    </div>
  );
};

Header.propTypes = {
  activeUser: PropTypes.object,
  logOutUser: PropTypes.func,
  setActiveMatch: PropTypes.func,
  fetchNotifications: PropTypes.func
};

Header.defaultProps = {
  activeUser: {},
  logOutUser: () => {},
  setActiveMatch: () => {},
  fetchNotifications: () => {}
};

export default Header;
