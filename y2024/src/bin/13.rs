use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use regex::Regex;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "13"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279";

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn part1<R: BufRead>(mut reader: R) -> Result<usize> {
        let regex = Regex::new(r"(?m)Button A.*\+(\d+),.*\+(\d+)\s+Button B.*\+(\d+),.*\+(\d+)\s+Prize.*=(\d+),.*=(\d+)").unwrap();
        let mut text = String::new();
        reader
            .by_ref()
            .read_to_string(&mut text)
            .expect("TODO: panic message");

        let result = regex.captures_iter(&text);
        let mut answer = 0;
        for mat in result {
            let (_, [a_x, a_y, b_x, b_y, p_x, p_y]) = mat.extract();
            let p_x = p_x.parse::<f64>().unwrap();
            let p_y = p_y.parse::<f64>().unwrap();
            answer += solve_2x2(a_x, a_y, b_x, b_y, p_x, p_y, 100.0);
        }
        Ok(answer)
    }
    fn solve_2x2(
        a_x: &str,
        a_y: &str,
        b_x: &str,
        b_y: &str,
        p_x: f64,
        p_y: f64,
        limit: f64,
    ) -> usize {
        let a_x = a_x.parse::<f64>().unwrap();
        let a_y = a_y.parse::<f64>().unwrap();
        let b_x = b_x.parse::<f64>().unwrap();
        let b_y = b_y.parse::<f64>().unwrap();

        // [ ax  bx][ x ]     [ px ]
        // [ ay  by][ y ]  =  [ py ]

        // inverse
        // [ by  -bx ]
        // [-ay  ax ]
        let determinant: f64 = a_x * b_y - b_x * a_y;
        let a: f64 = (1f64 / determinant) * ((b_y * p_x) + (-1_f64 * b_x * p_y));
        let b: f64 = (1f64 / determinant) * ((-1_f64 * a_y * p_x) + (a_x * p_y));

        if a < 0.0 || b < 0.0 || a > limit || b > limit {
            return 0;
        }

        if (a.fract() <= 0.01_f64 || a.fract() >= 0.99_f64)
            && (b.fract() <= 0.01_f64 || b.fract() >= 0.99_f64)
        {
            return 3 * a.round() as usize + b.round() as usize;
        }
        0
    }

    assert_eq!(480, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(mut reader: R) -> Result<usize> {
        let regex = Regex::new(r"(?m)Button A.*\+(\d+),.*\+(\d+)\s+Button B.*\+(\d+),.*\+(\d+)\s+Prize.*=(\d+),.*=(\d+)").unwrap();
        let mut text = String::new();
        reader
            .by_ref()
            .read_to_string(&mut text)
            .expect("TODO: panic message");

        let result = regex.captures_iter(&text);
        let mut answer = 0;
        for mat in result {
            let (_, [a_x, a_y, b_x, b_y, p_x, p_y]) = mat.extract();
            let p_x = p_x.parse::<f64>().unwrap() + 10000000000000.0;
            let p_y = p_y.parse::<f64>().unwrap() + 10000000000000.0;
            answer += solve_2x2(a_x, a_y, b_x, b_y, p_x, p_y, 1000000000000000.0);
        }
        Ok(answer)
    }

    // assert_eq!(0, part2(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
