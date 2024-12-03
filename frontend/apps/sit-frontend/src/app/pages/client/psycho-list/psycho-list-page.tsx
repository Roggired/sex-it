import './psycho-list.scss';
import { psychoProfileApi } from 'apps/sit-frontend/src/app/api/psycho/psycho-profile-api';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { SuiInput } from 'apps/sit-frontend/src/app/sui/sui-input/sui-input';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';
import Alla from '../../../../assets/img.png';
import {Page} from "../../shared/page/page";
import {useState} from "react";
import {PaginationSingle} from "../../../sui/icons/pagination/pagination-single";
import {GetPsychoListFilters} from "../../../api/psycho/model";
import {RatingWidget} from "../../shared/rating-widget/rating-widget";
import {PaginationDouble} from "../../../sui/icons/pagination/pagination-double";

export const PsychoListPage = () => {
  const [name, setName] = useState<string | undefined>(undefined)
  const [priceFrom, setPriceFrom] = useState<number | undefined>(undefined)
  const [priceTo, setPriceTo] = useState<number | undefined>(undefined)
  const [minRating, setMinRating] = useState<number | undefined>(undefined)

  const [filters, setFilters] = useState<GetPsychoListFilters>({});

  const [pageNumber, setPageNumber] = useState(0)
  const { data} = psychoProfileApi.useGetPsychoListQuery({
    body: {
      filters: filters
    },
    pageNumber: pageNumber,
    pageSize: 6
  });

  if (!data) {
    return <></>;
  }

  const onSearch = () => {
    setFilters({
      name: name,
      priceFrom: priceFrom,
      priceTo: priceTo,
      minRating: minRating
    });
  }

  return (
    <Page>
      <div className="psycho-list">
        <div className="psycho-list__sidebar">
          <SuiInput containerClass="psycho-list__filters__fio-input" label="ФИО" value={name} onChange={(e) => setName(e.target.value)}/>
          <div style={{ display: "flex", flexDirection: "row", gap: "24px" }}>
            <SuiInput containerClass="psycho-list__filters__price-input" label="Цена от" type="number" value={priceFrom} onChange={(e) => setPriceFrom(+e.target.value)}/>
            <SuiInput containerClass="psycho-list__filters__price-input" label="Цена до" type="number" value={priceTo} onChange={(e) => setPriceTo(+e.target.value)}/>
          </div>
          <SuiInput containerClass="psycho-list__filters__rating-input" label="Мин рейтинг" type="number" value={minRating} onChange={(e) => setMinRating(+e.target.value)}/>
          <SuiButton onClick={onSearch}>Поиск</SuiButton>
        </div>
        <div className="psycho-list__main">
          <div className="psycho-list__list">
            {data.content.map((value) => (
              <PsychoCard
                key={value.id}
                id={value.id}
                name={value.name}
                price={value.price}
                rating={value.rating ?? 0}
              />
            ))}
          </div>
          <div className="psycho-list__btns">
            <PaginationDouble enabled={false} onClick={() => ''}/>
            <PaginationSingle enabled={pageNumber > 0} onClick={() => setPageNumber(pageNumber - 1)}/>
            {pageNumber + 1}
            <PaginationSingle enabled={pageNumber < data.totalPages - 1} onClick={() => setPageNumber(pageNumber + 1)} isRightRotated={true}/>
            <PaginationDouble enabled={false} onClick={() => ''} isRightRotated={true}/>
          </div>
        </div>
      </div>
    </Page>
  );
};

const PsychoCard = ({
                      id,
                      rating,
                      name,
                      price,
                    }: {
  readonly id: number;
  readonly name: string;
  readonly price: number;
  readonly rating: number;
}) => {
  const navigate = useNavigate();

  return (
    <div
      className="psycho-list__card"
      onClick={() => navigate(routes.toClientPsychoCard(id))}
    >
      <img src={Alla}/>
      <h3>{name}</h3>
      <span>Цена за час: {price} руб.</span>
      <div style={{ display: "flex", flexDirection: "row", gap: "4px", alignItems: "center"}}>
        <span>Рейтинг: </span>
        <RatingWidget rating={rating}/>
      </div>
    </div>
  );
};
