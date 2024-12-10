import './friend-create-profile.scss';
import {useEffect, useState} from 'react';
import {Page} from '../../shared/page/page';
import {SuiInput} from '../../../sui/sui-input/sui-input';
import {SuiButton} from '../../../sui/sui-button/sui-button';
import {friendProfileApi} from 'apps/sit-frontend/src/app/api/friend/friend-profile-api';
import {SuiLoader} from "../../../sui/sui-loader/sui-loder";
import {useNavigate} from "react-router-dom";
import {routes} from "../../../utils/routes";

export const FriendCreateProfile = () => {
  const navigate = useNavigate();
  const [id, setId] = useState<number | undefined>();
  const [name, setName] = useState<string | undefined>();
  const [email, setEmail] = useState<string | undefined>();
  const [percent, setPercent] = useState(0);

  const [error, setError] = useState<string | undefined>()

  const { data: currentProfile, isLoading} = friendProfileApi.useGeyMyProfileQuery();
  const [updateProfile] = friendProfileApi.useCreateFriendMutation();

  useEffect(() => {
    console.log("her")
    if (currentProfile) {
      setId(currentProfile.id)
      setName(currentProfile.name)
      setEmail(currentProfile.email)
      setPercent(1)
    }
  }, [isLoading, currentProfile]);

  const onUpdateProfile = () => {
    if (name && email) {
      updateProfile({
        id: id ?? 0,
        body: {
          name,
          email,
          percent,
        },
      }).then((_) => navigate(routes.toCreateFriendShipPage()))
    } else {
      setError("Имя и почта должны быть заполнены")
    }
  }

  const onReset = () => {
    if (currentProfile) {
      setId(currentProfile.id)
      setName(currentProfile.name)
      setEmail(currentProfile.email)
      setPercent(1)
    } else {
      setId(undefined)
      setName(undefined)
      setEmail(undefined)
      setPercent(1)
    }
  }

  if (isLoading) {
    return <Page center={true}>
      <SuiLoader/>
    </Page>
  }

  return (
    <Page>
      <div className="friend-create-profile">
        <div className="friend-create-profile__header">
          <div className="friend-create-profile__main">
            <SuiInput
              label="Имя"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
            <SuiInput
              label="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
        </div>
        <div>
          {error && <span style={{color: "red"}}>{error}</span>}
        </div>
        <div className="friend-create-profile__buttons">
          <SuiButton
            onClick={onUpdateProfile}
          >
            Сохранить
          </SuiButton>
          <SuiButton
            onClick={onReset}
            buttonType="secondary"
          >
            Отменить
          </SuiButton>
        </div>
      </div>
    </Page>
  );
};
